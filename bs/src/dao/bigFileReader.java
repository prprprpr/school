package dao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class bigFileReader {
	private int threadSize;
	private String charset;
	private int bufferSize;
	private IHandle handle;
	private ExecutorService excutorService;
	private long fileLength;
	private RandomAccessFile rAccessFile;
	private RandomAccessFile wAccessFile;
	private Set<StartEndPair> startEndPairs;
	private CyclicBarrier cyclicBarrier;
	private AtomicLong counter=new AtomicLong(0);
	private File file;
	private File out;
	
	private bigFileReader(File file,File out,IHandle handle,String charset,int buffersize,int threadSize){
		this.fileLength=file.length();
		this.handle=handle;
		this.charset=charset;
		this.bufferSize=buffersize;
		this.threadSize=threadSize;
		this.file=file;
		this.out=out;
		try{
			this.rAccessFile=new RandomAccessFile(file,"r");
			this.wAccessFile=new RandomAccessFile(out,"rw");
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		this.excutorService=Executors.newFixedThreadPool(threadSize);
		startEndPairs=new HashSet<bigFileReader.StartEndPair>();
	}
	
	public void start(){
		long everySize=this.fileLength/this.threadSize;
		try{
			calculateStartEnd(0,everySize);
		}catch(IOException e){
			e.printStackTrace();
			return;
		}
		
		final long startTime=System.currentTimeMillis();
		cyclicBarrier=new CyclicBarrier(startEndPairs.size(),new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("use time:"+(System.currentTimeMillis()-startTime)/1000);
				System.out.println("all line:"+counter.get());
			}	
		});
		
		for(StartEndPair pair:startEndPairs){
			System.out.println("分配分片："+pair);
			this.excutorService.execute(new SliceWriterTask(pair));
		}
	}
	
	private void calculateStartEnd(long start,long size) throws IOException{
		if(start>fileLength-1){
			return;
		}
		StartEndPair pair=new StartEndPair();
		pair.start=start;
		long endPosition=start+size-1;
		if(endPosition>fileLength-1){
			pair.end=fileLength-1;
			startEndPairs.add(pair);
			return;
		}
		rAccessFile.seek(endPosition);
		byte temp=(byte) rAccessFile.read();
		while(temp!='\n'&&temp!='\r'){
			endPosition++;
			if(endPosition>=fileLength-1){
				endPosition=fileLength-1;
				break;
			}
			rAccessFile.seek(endPosition);
			temp=(byte) rAccessFile.read();
		}
		pair.end=endPosition;
		startEndPairs.add(pair);
		calculateStartEnd(endPosition+1,size);
	}
	public static class StartEndPair{
		public long start;
		public long end;
		
		public String toString(){
			return "start="+start+";end="+end;
		}
		
		public int hashCode(){
			final int prime=31;
			int rs=1;
			rs=prime*rs+(int)(end^(end>>>32));
			rs=prime*rs+(int)(start^(start>>>32));
			return rs;
		}
		
		public boolean equals(Object obj){
			if(this==obj){
				return true;
			}
			if(obj==null){
				return false;
			}
			if(getClass()!=obj.getClass()){
				return false;
			}
			StartEndPair other=(StartEndPair) obj;
			if(end!=other.end){
				return false;
			}
			if(start!=other.start){
				return false;
			}
			return true;
		}
	}
	
	public void shutdown(){
		try{
			this.rAccessFile.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		this.excutorService.shutdown();
	}
	private void handle(byte[] bytes) throws IOException{
		String line=null;
		if(this.charset==null){
			line=new String(bytes);
		}else{
			line=new String(bytes,charset);
		}
		if(line!=null&&!"".equals(line)){
			this.handle.handle(line);
			counter.incrementAndGet();
		}
	}
	private class SliceReaderTask implements Runnable{
		private long start;
		private long sliceSize;
		private byte[] readBuff;
		
		public SliceReaderTask(StartEndPair pair){
			this.start=pair.start;
			this.sliceSize=pair.end-pair.start+1;
			this.readBuff=new byte[bufferSize];
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				MappedByteBuffer mapBuffer=rAccessFile.getChannel().map(MapMode.READ_ONLY, start, this.sliceSize);
				ByteArrayOutputStream bos=new ByteArrayOutputStream();
				for(int offset=0;offset<sliceSize;offset+=bufferSize){
					int readLength;
					if(offset+bufferSize<=sliceSize){
						readLength=bufferSize;
					}else{
						readLength=(int) (sliceSize-offset);
					}
					mapBuffer.get(readBuff, 0, readLength);
					for(int i=0;i<readLength;i++){
						byte temp=readBuff[i];
						if(temp=='\n'||temp=='\r'){
							handle(bos.toByteArray());
							bos.reset();
						}else{
							bos.write(temp);
						}
					}
				}
				if(bos.size()>0){
					handle(bos.toByteArray());
				}
				cyclicBarrier.await();
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				shutdown();
			}
		}
	}
	
	private class SliceWriterTask implements Runnable{
		private long start;
		private long sliceSize;
		private byte[] readBuff;
		
		public SliceWriterTask(StartEndPair pair){
			this.start=pair.start;
			this.sliceSize=pair.end-pair.start+1;
			this.readBuff=new byte[(int) sliceSize];
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			RandomAccessFile reader=null;
			RandomAccessFile writer=null;
			try{
				reader=new RandomAccessFile(file,"r");
				writer=new RandomAccessFile(out,"rw");
				reader.seek(start);
				reader.read(readBuff);
				writer.write(readBuff);
				cyclicBarrier.await();
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				shutdown();
				try {
					reader.close();
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static class Builder{
		private int threadSize=1;
		private String charset=null;
		private int bufferSize=1024*1024;
		private IHandle handle;
		private File file;
		private File out;
		public Builder(String file,String out,IHandle handle){
			this.file=new File(file);
			if(!this.file.exists()){
				throw new IllegalArgumentException("文件不存在！");
			}
			this.out=new File(out);
			this.handle=handle;
		}
		public Builder withThreadSize(int size){
			this.threadSize=size;
			return this;
		}
		public Builder withCharset(String charset){
			this.charset=charset;
			return this;
		}
		public Builder withBufferSize(int bufferSize){
			this.bufferSize=bufferSize;
			return this;
		}
		public bigFileReader build(){
			return new bigFileReader(this.file,this.out,this.handle,this.charset,this.bufferSize,this.threadSize);
		}
	}
}
