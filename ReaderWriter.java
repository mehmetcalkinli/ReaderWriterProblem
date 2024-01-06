package ReaderWriterProblem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import ReaderWriterProblem.ReadWriteLock;
import ReaderWriterProblem.Reader;
import ReaderWriterProblem.Writer;

public class ReaderWriter {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
				
       

        Semaphore semaphore = new Semaphore(1);
        Semaphore readerSem = new Semaphore(1);
        
        ReadWriteLock RW = new ReadWriteLock(semaphore,readerSem);
		
		executorService.execute(new Writer(RW,"WriterThread1"));
		executorService.execute(new Writer(RW, "WriterThread2"));
		executorService.execute(new Writer(RW, "WriterThread3"));
		executorService.execute(new Writer(RW, "WriterThread4"));
		
		executorService.execute(new Reader(RW, "ReaderThread1"));
		executorService.execute(new Reader(RW, "ReaderThread2"));
		executorService.execute(new Reader(RW, "ReaderThread3"));
		executorService.execute(new Reader(RW, "ReaderThread4"));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();

        
    }
}

class ReadWriteLock {

    //private static Semaphore semaphore = new Semaphore(1);
    //private static Semaphore readerSem = new Semaphore(1);
    private int readerCount = 0;
    Semaphore semaphore;
    Semaphore readerSem;



    public ReadWriteLock(Semaphore semaphore,Semaphore readerSem) {
            this.semaphore = semaphore;
            this.readerSem = readerSem;
    }

    public void readLock(String threadName) {
        try {
            

            semaphore.acquire();
            
            readerCount++;
            
            if (readerCount == 1) {
                readerSem.acquire();
            }

            
            semaphore.release();

            System.out.println(threadName + " successfully accessed data for reading.");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void readUnLock(String threadName) {
        try {

            semaphore.acquire();
            readerCount--;
            

            if (readerCount == 0) {
                readerSem.release();
            }
            
            semaphore.release();

            System.out.println(threadName + " successfully released data after reading.");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void writeLock(String threadName) {

      

        try {
            
            //semaphore.acquire();
            
            readerSem.acquire(); 
            System.out.println(threadName + " successfully accessed data for writing.");
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void writeUnLock(String threadName) {
        //semaphore.release();
        System.out.println(threadName + " successfully released data after writing.");

        readerSem.release(); 

    }
}




class Writer implements Runnable
{
   private ReadWriteLock RW_lock;
   private String threadName;



   

    public Writer(ReadWriteLock rw, String string) {
    	RW_lock = rw;
        this.threadName = string;

   }

    public void run() {
      while (true){

        try {
                RW_lock.writeLock(threadName);
                Thread.sleep(5000); //Work
                RW_lock.writeUnLock(threadName);
                break; 
            } catch (Exception e) {                
                e.printStackTrace();
            }
    	  
       
      }
   }


}



class Reader implements Runnable
{
   private ReadWriteLock RW_lock;
   private String threadName;

  

   public Reader(ReadWriteLock rw, String string) {
    	RW_lock = rw;
        this.threadName = string;

   }
   public void run() {
      while (true){

        try {
                RW_lock.readLock(threadName);
                Thread.sleep(5000); //Work
                RW_lock.readUnLock(threadName);
                break; 
            } catch (Exception e) {
                e.printStackTrace();
            }
        
       
      }
   }
    

}   






