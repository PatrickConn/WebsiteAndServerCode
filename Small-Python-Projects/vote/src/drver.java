
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
	

class Drivers 
{

  // Student Name : patrick connaughton
  // Student Id Number : 	C00167985
	
  
    public static void main(String[] args)

    {	
    	
    	int Continue = 0; 
    	// this will loop as long as the use wont too
    	do{
    	System.out.print("\nWelcome vote-counting application for a simple electoral system");
      		voteCounting can[]; 
      		can = new voteCounting[100] ;
			String theName  ;  
    		String Party  ; 
    		int index;
    		int numOfCandidates = 0;
			int rondsOfVoteing = 0;		
			int sum = 0;
			int i =0;
			int vote =0;
			int candidatesMaxVotes=0;
			int CandidatesIndex = 0;
			int j=0;  
			
    		@SuppressWarnings("resource")
			Scanner reader = new Scanner(System.in);
    		// this will ask the user how many Candidates they wont and BallotPapers they wont 
    		// I put the Candidates in to the array can
			System.out.print("\nEnter in the number of Candidates ->");
			numOfCandidates =reader.nextInt();
			System.out.print("\nEnter in the number of BallotPapers you wont ->");
			rondsOfVoteing =reader.nextInt();
			int[] count = new int [numOfCandidates];
			// this is the way is create my BallotPaper a 2D array 
			int[][] BallotPaper = new int [numOfCandidates][rondsOfVoteing];
			//this is a back up ballot paper just in caes 
			int[][] BackUpBallotPaper = new int [numOfCandidates][rondsOfVoteing];
			// this will read in the candidates name and party
			// and will put them in too the array can
			reader.nextLine();
			for (index = 0; index < numOfCandidates ; index ++)
			{			
			System.out.print("\nEnter in the  name ->");
			theName =reader.nextLine();				
			System.out.print("\nEnter in the Party ->");
			Party = reader.nextLine();
			can[index] = new voteCounting(theName ,Party);	    	   	
			}
			i=0;
			// this will output the Candidates and read in there voet for that Candidates 
			// in that rond
			while ( i < rondsOfVoteing)
			{
				for ( j = 0; j < numOfCandidates; j++)
				{
					//this will out put the Candidates and read in there vote for the BallotPaper 
					System.out.print("\nEnter in your vote for-> "+can[j].getName()+"\nFor BallotPaper " +i+" -> ");
					vote = reader.nextInt();	
					BallotPaper[j][i] = vote;
					// when the BallotPaper is fill out we ask the for the next voter
						if(j == numOfCandidates -1)
						{
							System.out.print("\nNext voter places ");
						}
					}
				i++;
			}
		//this will see if the number of candidates = 1 and if it dose then it will 
			// say they are the winner and see if they wont to stat over angrn 
	if (numOfCandidates == 1)
		{
		System.out.print("\nThe winning candidate is -> " + can[0].getName());
		System.out.print("\nThe winning candidate Party is -> " + can[0].getParty()); 
		
		}
	else
	{
		
		
			
		
		do{
		//ths will count the candidates votes in all the BallotPapers;
		for (i = 0; i < numOfCandidates; i++) 
		{
		    sum = 0;
 		    for ( j = 0; j < rondsOfVoteing; j++) 
 		    {
 		    	
	 		    if(BallotPaper[i][j] == 1 )
	 		    {
	 		    sum  = sum + 1;
	 		    }
	 		    else
	 		    {
	 		    sum = sum + 0;
	 		    }
 		    }
 		   count [i] = sum;
 		}
		/*this will output the sun of all the candidates ronds of voteing
		for (i=0; i < numOfCandidates ; i++)
		{
			System.out.println(count[i]);
		}
		*/
	
		candidatesMaxVotes = 100;
	// this will see who has the most votes and keep there index 
	int	k = 0;
 	for ( k = 0; k < numOfCandidates; k++)
 	{
 		
 		if( count[k] <= candidatesMaxVotes  )
 		{
 			candidatesMaxVotes = count[k];
 			CandidatesIndex = k;
 		}
 	}
 
 	// this will print the canidate that will be delete form the electoral
 	System.out.print("\nThe candidate that will be delete is -> " + can[CandidatesIndex].getName());
 	System.out.print("\n");
 	// this will print all the BallotPaper befor you updata it and delete a row form it
 	for( i = 0; i < BallotPaper.length; i++)
 	   {
 		 System.out.println("Canidate -> "+ can[i].getName()+" votes ");
 	      for( j = 0; j < BallotPaper[i].length; j++)
 	      {
 	    		
 	         System.out.printf("%5d ", BallotPaper[i][j]);
 	      }
 	      System.out.println();
 	   }
 	 System.out.println("Press any key to continue...");
     try {
		System.in.read();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 // this will call the updata method
 	BallotPaper = upDataAarray(BallotPaper,CandidatesIndex);
 	System.out.print("\n");
 	// this will call the DeletAarray method
 	BallotPaper = DeletAarray(BallotPaper,CandidatesIndex);
 	System.out.print("\n");
 	// this will delete the Candidates form the array can
 	for(i=CandidatesIndex; i< numOfCandidates ; i++)
 	{
 		can[i] = can[i+1];
 	}
 	// this will updata numOfCandidates till it hits 1
 	numOfCandidates--;
 					// this will print out the winning candidate 
				 	if(numOfCandidates == 1)
				 	{
				 		System.out.print("\nThe winning candidate is -> " + can[0].getName());
				 		System.out.print("\nThe winning candidate Party is -> " + can[0].getParty());
				 	}
		}while(numOfCandidates != 1);
    }
 		// this will ask the user if they wont to start over if yes it will go back to start
		System.out.print("\nDo you wish to start over");
		System.out.print("\nEnter 0 for eixt / Enter 1 to go agen");
		Continue =reader.nextInt();
    
    	}while(Continue != 0) ;	
    	System.out.print("\nOK goodbye voter");
 
   }	// main
    		
		
    // this is the method that will delete a row form a 2D array BallotPaper
    public static int[][] DeletAarray(int[][] BallotPaper ,int num){
    	 int temp = num;
    	 // this will put the array in to a list so it can be delete the row
    	List<int[]> l = new ArrayList<int[]>(Arrays.asList(BallotPaper));
    	// this will remove the line in the list that you wont
       	l.remove(temp);
       	// then it will put the updata list without the line you do not wont 
       	// and put it in to a new 2D array and send it back
       	BallotPaper = l.toArray(new int[][]{});
    
       	// this will return the new ballotpaper with the delete row
		return BallotPaper;

    	} 
    
 // this is the method that will up data all the columns of BallotPaper 2D array
    public static int[][] upDataAarray(int[][] BallotPaper ,int num ){
    	 
     int downOne;
     int less;
     int j=0;
     int index=0;		
     			// this loop all the rows one by one till you hit the last one in the row
 				for ( j = 0; j <= BallotPaper[num].length -1; j++)// for loop row 
	    		{
 					// this keep the vote in the row of you Candidates you wish to delete 
 					less = BallotPaper[num][j]; 
 								//this will strart at the frist at top of colum and loop down
		        		    	 for(index=0; index <= BallotPaper.length -1; index++) // this will up date the col 
		        		    	 {
		        		    		 //the if will see if the current vote is grater than the one in the row 
		        		    		 // you are going to delete if so it will go in 
			        		    	 if( BallotPaper[index][j] > less)
			        		    	 {
			        		    		//sets downOne to 0 
			        		    		 downOne = 0;
			        		    		 // put the current vote int to downOne
			        		    		 downOne = BallotPaper[index][j];
			        		    		 // it will uo the vote donw on
			        		    		 //E.G if the curent vote = 4
			        		    		 //and the vote in the row you are going to delete = 2
			        		    		 // then the curent vote vote will = 3
			        		    		 downOne = downOne -1;
			        		    		 // and put the new updataed vote back in to the BallotPaper
			        		    		 BallotPaper[index][j] = downOne;
			        		    	 }//if( BallotPaper[index][j] > less)	
		        		    	 }//for loop number 2   		    
		        }// for loop number 1
 				// this retunt the new BallotPaper
		return BallotPaper;
    	} 
 } // class
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
  	
