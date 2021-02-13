/*

*/
import java.lang.Math;
import java.io.*;
import java.util.Scanner; 
import java.io.FileWriter;
class Queue
{
final static int Q_LIMIT=100;/*Limit on queue length*/
final static int BUSY=1;/*mnemonics for servers being busy*/
final static int IDLE=0;/*mnemonics for servers being idle*/
static int next_event_type,num_custs_delayed,num_events,num_in_q,server_status;
static float area_num_in_q,area_server_status,num_delays_required,mean_interarrival;
static float mean_service,time,time_last_event,total_of_delays;
static float time_arrival[]=new float[Q_LIMIT+1];
static float time_next_event[]=new float[3];

static void initialize()
{
//initialize simulation clock
time=0;
//initialize the state variables
server_status=IDLE;
num_in_q=0;
time_last_event=0;
//initialize statistical counters
num_custs_delayed=0;
total_of_delays=0;
area_num_in_q=0;
area_server_status=0;
/* Initialize event list. Since no customers are present, the depature(service completion)event is eliminated from 
consideration*/
time_next_event[1] = time + expon(mean_interarrival);
time_next_event[2]=(float)Math.pow(10, 30);
}
static void  timing()//timing function
{
int i;
float min_time_next_event=(float)Math.pow(10, 29);
next_event_type=0;
// Determine the event type of the next event to occur.

for(i=1;i<=num_events;i++)
{
if(time_next_event[i]<min_time_next_event)
{
min_time_next_event=time_next_event[i];
next_event_type=i;
}
}
//check to see whether the event list is empty
if(next_event_type==0)
{
//the event list is empty so stop the simulation
try{FileWriter myWriter = new FileWriter("mm1.out.txt",true);
 myWriter.write("\n event list is empty at time:"+time+"\n\n");
myWriter.close();
                // Terminate JVM 
                System.exit(0); 
                }catch(Exception e){System.out.println(e);}    
}

//the event list is not empty,so advance the simulation clock

time=min_time_next_event;

}
static void arrive()
{
float delay;
//schedule next arrival
time_next_event[1] = time + expon(mean_interarrival);
//check to see whether server is busy
if(server_status==BUSY)
{
//server is busy so increment number of customers in queue
num_in_q++;
//check to see whether an overflow condition exists
if(num_in_q>Q_LIMIT)
{
//queue has overflowed,so stop the simulation
	try{	FileWriter myWriter = new FileWriter("mm1.out.txt",true);
 myWriter.write("\n overflow of the array time_arrival at:"+time+"\n\n");
myWriter.close();
                // Terminate JVM 
                System.exit(2); }
                catch(Exception e)
              {
                System.out.println(e); 
              }
}
/*There is still room in the queue. so store the time of
arrival of the arriving customer at the (new) end  of time_arrival.*/
time_arrival[num_in_q]=time;
}
else
{
/*Server is idle, so arriving customer has a delay of zer.
(The following two statements are for program clarity and do
not affect the results of the simulation)*/
delay=0;
total_of_delays=total_of_delays+delay;
/*Increment the number of customers delayed,and make
server busy*/
num_custs_delayed++;
server_status=BUSY;
//Schedule a departure (serviece completion.
time_next_event[2] = time + expon(mean_service);
}

}
static void depart()//departure function
{
int i;
float delay;
//check to see whether queue is empty
if(num_in_q==0)
{
/* the queue is empty so make the server idle and eliminate the
departure(service completion) event from consideration.*/
server_status=IDLE;
time_next_event[2]=(float)Math.pow(10,30);
}
else
{
/*The queue is non empty, so decrement the number of customers in 
queue*/
num_in_q--;
/* Compute the delay of the customer who is beginning service
and update the total delay accumulator. */
delay=time-time_arrival[1];
total_of_delays=total_of_delays+delay;
/* Increment the number of customers delayed, and schedule
departure. */
num_custs_delayed++;
time_next_event[2] = time + expon(mean_service);
//move each customer in queue(if any) up one place.
for(i=1;i<=num_in_q;i++) time_arrival[i]=time_arrival[i+1];


}
}

static void report()//report generator function
{
//compute and write estimates of desired measures of perfomance
	try{FileWriter myWriter = new FileWriter("mm1.out.txt",true);
 myWriter.write("\n\naverage delay in queue:"+total_of_delays/num_custs_delayed+" minutes\n\n");
 myWriter.write("\n\naverage number in queue:"+area_num_in_q/time+" \n\n");
  myWriter.write("\n\nServer utilization:"+area_server_status/time+" \n\n");
 myWriter.write("\n\nTime simulation ended:"+time+"\n\n");
myWriter.close();}catch(Exception e){System.out.println(e);}    
}
static void update_time_avg_stats()//update area accumulators for time-average statistics
{
float time_since_last_event;
/*compute time since last event,and update last-event-time
marker*/
time_since_last_event=time-time_last_event;
time_last_event=time;
//update area under number-in-queue function
area_num_in_q+=num_in_q*time_since_last_event;
//update area under server-busy indicator function
area_server_status+=server_status*time_since_last_event;
}
static float expon(float mean)//exponential variate generation function
{
float u;
//generate a U(0,1) random variate
u=(float)Math.random();
//return an exponential random variate with mean "mean".
return (float)(-mean*Math.log(u));
}
public static void main(String args[]) {
	try{
		FileWriter myWriter1 = new FileWriter("mm1.out.txt",true);
	  
num_events=2;
Scanner scanner = new Scanner(new File("mm1.in.txt"));
float [] tall = new float [3];
int i = 0;
while(scanner.hasNext())
{
     tall[i] = Float.parseFloat(scanner.next());
    // System.out.println(tall[i]);
     i++;
}
mean_interarrival=tall[0];
mean_service=tall[1];
num_delays_required=tall[2];
    

     try
     { myWriter1.write("single-server queueing system\n\n");
 }catch(Exception e){System.out.println(e);}    
   try
   {myWriter1.write("mean interarrival time:"+mean_interarrival+" minutes\n\n"); 
   myWriter1.write("mean service time:"+mean_service+" minutes\n\n");
}catch(Exception e){System.out.println(e);}    
 try{myWriter1.write("Number of customers:"+num_delays_required+"\n\n");
}catch(Exception e){System.out.println(e);}    

try
{scanner.close();
myWriter1.close();
}catch(Exception e){System.out.println(e);}    
//initialize simulation
initialize();
//run the simulation while more delays are still needed
while(num_custs_delayed<num_delays_required)
{
//determine the next event
timing();
//update time-average statistical accumulators.
update_time_avg_stats();
//invoke the appropriate event function
switch (next_event_type)
{
case 1:
arrive();
break;
case 2:
depart();
break;
}

}

//invoke the report generator and end the simulation.
report();

}catch(Exception e){System.out.println(e);}    

}
}
