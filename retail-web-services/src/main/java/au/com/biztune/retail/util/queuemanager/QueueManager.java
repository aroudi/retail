package au.com.biztune.retail.util.queuemanager;

import au.com.biztune.retail.util.Environment;
import au.com.biztune.retail.util.IdBConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueManager implements Runnable
{
    private static QueueManager instance;
    private static Queue<Request> queue;

    private Processor processor;
    //an internal thread for handling online queue
    private Thread onlineHandler = null;

  /*
  * Flag which is set when shutdown of
  * the background thread is required.
 	* Volatile because the thread using the
  * flag and the thread setting it to true
 	* are different threads, and it is
  * important that the flag is not held in
 	* CPU registers, or one thread will
  * see a different value to the other.
  */
	
    //private volatile static boolean transmitFlag = false;
    public static long onlineThreadTimeToDie = 1000;
    public static int maxAttempt = 2;
    private final Logger logger = LoggerFactory.getLogger(QueueManager.class);

    public QueueManager() {
        init();
    }

    public static synchronized QueueManager getInstance()
	{
		if (instance == null)
		{
			instance = new QueueManager();
			instance.init();
		}
		return instance;
	}

	public synchronized void push(Request item)
	{
        if (item != null) {
            queue.offer(item);
        }
		// call for processing the new arrival requests...
        //set online mode
        //setOnlineMode(true);
        onlineQueueManagerActivate();
	}
	
	public synchronized void processQueue()
	{
		// check if processor is not null and the onlineQueue is not empty.
        logger.debug("QueueManager: queue = " + queue + " queue.size = " + queue.size());
		if (queue!=null && !queue.isEmpty())
		{
            while (!queue.isEmpty())
            {
                Request item = queue.poll();
                if (item == null) {
                    continue;
                }
                Response response = null;
                try
                {
                  //item.addAttempt();
                  processor = item.getProcessor();
                  if (processor == null) {
                      logger.error("processor for item is null. skept the item");
                      continue;
                  }
                  response = processor.process(item);
                }
                catch (Exception e)
                {
                  logger.error("Exception for the ", e);
                }
                finally{
                  // on case of fail
                  if ( (response == null) || ((response != null && response.getMessage()!=null) && ( !response.getMessage().equals(IdBConstant.BAD_REQUEST) || !response.getMessage().equals(IdBConstant.BAD_REQUEST ) && !( response.isSucceeded()) )))
                  {
                      if (item.getAttempt() <= maxAttempt) {
                          queue.offer(item);
                      }
                  }
                }
            }
        }
	}
	

  public synchronized void init()
	{
		queue = new ConcurrentLinkedQueue<Request>();
        try {
            String onlineThreadTimeToDieStr = Environment.get("QUEUE_THREAD_TIME_TO_DIE");
            String maxAttemptStr = Environment.get("QUEUE_MAX_ATTEMPT");
            onlineThreadTimeToDie = Long.getLong(onlineThreadTimeToDieStr);
            maxAttempt = Integer.getInteger(maxAttemptStr);
        } catch (Exception e) {

        }
    }
	
  public void run()
  {
        processQueue();
        this.onlineQueueManagerClose();
  }

  /**
  * Starts a timer thread for processing the offline queue
        *
        */
  // activate a thread for processing online queue
  public synchronized void onlineQueueManagerActivate(){
    try
    {
      if (onlineHandler == null)
        {
          onlineHandler = new Thread(this, "onlineThread");
          onlineHandler.start();    // start thread
          logger.info("online thread started ");
        }
      else
        {
          if (!onlineHandler.isAlive())
          {
            onlineHandler.start();
          }
        }
    }
    catch (Exception e)
    {
       logger.error("Error in starting online thread: ", e);
       //setOnlineMode(false);
       return;
    }
  }


  public synchronized void  onlineQueueManagerClose()
  {

    try {

        // Only wait for a certain time before giving up and timing out.
         onlineHandler.join( onlineThreadTimeToDie );

         // Free up the thread control block for garbage collection.
         onlineHandler = null ;
         logger.info("online thread terminated");

        }
    catch (InterruptedException e)
    {
        // Don't propogate the exception.
        // Assume that the thread will stop shortly anyway.
    }
 }

}
