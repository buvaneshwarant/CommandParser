# CommandParser
This is a Java program that executes a protocol from a text file. This program would takes the command file, removes comments and white spaces, and executes the commands. The resulting file is a file with a list of commands, without loops and comments. 
The acceptable keywords are:
  1. loop -- signifies the start of the loop command function.
  2. endloop -- signifies the end of the loop function
Everything else is a command. The commands inbetween the 'loop' and 'endloop' keyword are executed a set number of times determined by the digit after the loop keyword. 

The following commands for exaxmple:

        open door

        walk 

        loop 2

        climb stairs

        endloop

        walk

        open door

        close door

The output for the above command would be:

        open door

        walk 

        climb stairs

        climb stairs

        walk

        open door

        close door

Simillarly, we can have nested loops, or a combination of single loops and nested loops. 
For example, the following command:

        loop 2
  
        walk one step

        end loop

        open fridge

        loop 2

        grab snack

        loop 2

        eat snack

        endloop

        endloop

        close fridge
      
The output for the above commands is:

        walk one step

        walk one step

        open fridge

        grab snack

        eat snack
  
        eat snack

        grab snack

        eat snack

        eat snack

        close fridge
  
Comments start with '#'. The following lines, for example. 

        #eat snack

        drink water

The output is:

        drink water

As you can see the line with # is ignored. 

In summation, this program igrnores empty lines, ignores comments, and executes commands (parses an exact set of commands to a text file). The initial product only supports the 'loop' and 'endloop' keyword. The next releases will support the 'if' and 'endif' clauses. 
