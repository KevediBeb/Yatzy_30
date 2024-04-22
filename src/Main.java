import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //sets up game start (this code runs once)
        Scanner scan = new Scanner(System.in);
        System.out.println("How many players?");
        // defines player amount
        int players = 1;
        players = input(scan, 2, 100);


        // defines player arrays
        String names[] = new String[players];
        int lives[] = new int[players];
        // sets 30 lives to  each player
        for(int i = 0; i < players; i++){
            lives[i] = 30;
        }


        System.out.println("What are your names?");
        // defines player names in player array
        for(int i = 0; i < players; i++)
        {
            names[i] = scan.nextLine();
        }
        System.out.println("Your names are: "+Arrays.toString(names));

        System.out.println("Type [start] to start");
        // control bool
        boolean fail = true;
        // repeats until user types "start"
        while(fail){
            if(scan.nextLine().equals("start")){
                fail = false;
                // sets up variables for game start
                int round = 1;
                int turn = 1;
                // starts the first round
                roundStart(scan, names, lives, round, turn, players);
            }
        }

    }

    // Function checks input(int only) from user and loops if incorrect
    public static int input(Scanner scan, int lowerLim, int upperLim){
        //Variable setup
        int var = 0;
        //control variable
        boolean fail = true;
        while(fail){
            try {
                // tries to apply integer
                var = Integer.parseInt(scan.nextLine());
                // checks if number is within its limits
                if(var >= lowerLim && var <= upperLim){
                    fail = false;
                }else{
                    fail = true;
                    System.err.println("Number out of bounds, try again. (minimum: " + lowerLim + " maximum: " + upperLim + ")");
                }

            }
            catch (Exception e) {
                // if the check fails
                System.err.println("Expected number, try again.");

                fail = true;
            }
        }


        return var;
    }
    public static void roundStart(Scanner scan, String[] names, int[] lives, int round, int turn, int players){
        System.out.println("-------------------");
        System.out.println("Round " + round);
        // checks if more than 1 player is alive
        if(!checkDeath(names, lives, turn, scan)){
            // sets up the round
            System.out.println(names[turn - 1] + "´s " + "turn");
            // sets up dice
            int dice[] = new int[6];
            int diceLeft = 6;
            System.out.println("Type [roll] to roll dice");
            // control variable
            boolean fail = true;
            // checks if input is valid
            while(fail){
                if(scan.nextLine().equals("roll")){
                    fail = false;
                    int sum = roll(dice, diceLeft, scan);
                    int rest = 30 - sum;
                    // roller taking damage since sum is < 30
                    if(sum < 30){
                        lives[turn - 1] -= rest;
                        System.out.println(names[turn - 1] + " has taken " + rest + " damage for rolling under 30");
                        System.out.println(names[turn - 1] + "´s lives: " + lives[turn - 1]);

                    } else if (sum == 30) {
                        // skipping over since roller sum = 30
                        System.out.println("Sum is 30, commencing game");
                    }else{
                        // roller gets to attack since sum > 30
                        System.out.println("BEGIN THE ATTACK PHASE!!");
                        // sets up damage variable
                        int dmg = sum - 30;
                        attack(dmg, names, lives, turn, scan, players);
                    }
                    round++;
                    // loops the player order
                    if(turn == players){
                        turn = 0;
                    }
                    turn++;
                    // loops the function
                    roundStart(scan, names, lives, round, turn, players);
                }
            }

        }else{
            // if someone is out of lives
            System.out.println(names[turn-1] + " is out, jumping to next remaining player.");
            boolean gameOver = false;
            int deadPlayers = 0;
            // checks amount of dead players
            for(int i = 0; i < names.length; i++){
                if(lives[i] <= 0){
                    deadPlayers++;
                }
            }


            if(deadPlayers == players){
                System.out.println("Game Over");
            }else{
                if(turn == players){
                    turn = 0;
                }
                turn++;
                // loops the function
                roundStart(scan, names, lives, round, turn, players);
            }


        }

    }


    public static void attack(int rest, String[] names, int[] lives, int turn, Scanner scan, int players){
        // control variable
        boolean playerFail = true;

        while(playerFail){
            // dice variable
            int attackDice[] = new int[6];
            // damage variable
            int dmg = 0;
            System.out.println("Pick player who shall feel the wrath of " + names[turn-1]);
            // prints attackable players along with their numbers
            for(int i = 0; i < names.length; i++){
                if(names[i] != names[turn-1]){
                    System.out.println("Player " + (i + 1) + ": " + names[i]);
                }

            }
            // input player number
            int playerNum = input(scan, 1, players);
            // if the attacker targets themselves
            if(names[playerNum-1] == names[turn-1]){
                System.out.println("I mean if you really want to...");
            }

            System.out.println("Type [roll] to roll dice, every " + rest + " rolled is " + rest +  " life/lives taken from " + names[playerNum - 1]);
            // control variable
            boolean fail = true;
            while(fail) {
                if (scan.nextLine().equals("roll")) {
                    fail = false;
                    //loops through every dice
                    for(int i = 0; i < 6; i++){
                        // rolls the dice
                        attackDice[i] = (int)(Math.random()*(6-1+1)+1);
                        System.out.println("Dice " + (i + 1) + " = " + attackDice[i]);
                        // adds to the damage varaible
                        if(attackDice[i] == rest){
                            dmg += rest;
                        }
                    }
                }else{
                    fail = true;
                }
            }
            try{
                // damages targetted player
                playerFail = false;
                lives[playerNum - 1] -= dmg;
                System.out.println(names[playerNum-1] + " has taken " + dmg + " damage from " + names[turn-1]);
                System.out.println(names[playerNum-1] + "'s lives are now " + lives[playerNum - 1]);
            }catch (Exception e){
                // loops if target does not exist
                playerFail = true;
                System.err.println("Player does not exist, try again.");
            }
        }

    }

    public static boolean checkDeath(String[] names, int[] lives, int playerNum, Scanner scan){
        // control variable
        boolean playerDied = false;
        // checks if the player died
        if(lives[playerNum-1] <= 0){
           playerDied = true;
        }
        return playerDied;

    }
    public static int roll(int[] dice, int diceLeft, Scanner scan){
        // set up variables
        int sum = 0;
        int types = 0;
        // if there's remaining dice
        while(diceLeft > 0){

            // variable for previous dice, used for checking input later
            int prevDice = 0;
            // variable for how many dice the user keeps
            types = 0;
            System.out.println("Dice left : " + diceLeft);
            // rolls and prints each dice in dice array
            for(int i = 0; i < diceLeft; i++){
                dice[i] = (int)(Math.random()*(6-1+1)+1);
                System.out.println("Dice " + (i + 1) + " = " + dice[i]);
            }
            System.out.println("Which dice do you wish to keep? Type [stop] when done. (select at least one dice)");
            // control variable
            boolean fail = true;
            while(true){
                String line = scan.nextLine();
                // breaks out of loop if the user types "stop"
                if((line.equals("stop") && types > 0)){
                    break;
                }else{
                    // control variable
                    int test = 0;
                    // checks if input is an integer
                    try{
                        test = Integer.parseInt(line);
                        fail = false;
                    }catch (Exception e){
                        fail = true;
                    }
                    // if input is integer and does not go over or under current amount of dice
                    if(!fail && Integer.parseInt(line) <= diceLeft && Integer.parseInt(line) > 0){
                        // checks if the user tries to keep the same dice multiple times
                        if(Integer.parseInt(line) != prevDice){

                            // adds to variables
                            int diceNum = Integer.parseInt(line);
                            types++;
                            prevDice = diceNum;
                            sum += dice[diceNum-1];
                        }else{
                            // if user attempts to keep the same dice twice
                            System.err.println("Cannot pick same dice more than once");
                        }
                    }
                    else{
                        // input isn't a number
                        System.err.println("Not a dice number, try again.");
                    }
                    // checks if user uses all their types and breaks the typing loop
                    if(types >= diceLeft){
                        break;
                    }
                }


            }
            // subtracts diceLeft with types
            diceLeft -= types;
            // resets previous dice
            prevDice = 0;
            System.out.println("Types : " + types);
            System.out.println("Current sum: " + sum);
        }
        // dice round over
        System.out.println("-------------------");
        System.out.println("Dice round over");


        return sum;




    }





}