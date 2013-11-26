MapServer BADLY INCOMPLETE
==========================

Cycles trough steps, while using supplied models to calculate the results of 
the simulation.

        Server sends client UI                  ┐
    possible objects and attributes             ├ Client initialization
        + possible connections                  ┘
                  |                             
                 \ /                            
                                                
          User clicks away,                     ┐
     creating the world and it's                ├ Boring part
          initial properties                    │
         and presses simulate                   ┘
                  |                             
                 \ /                            
                                                
      Objects are created using                 ┐
       the user defined and or                  ├ Server initialization
           default values                       ┘
                  |                             
                 \ /                            
                                                
                Step 0 ───────┐                 ┐
                 Data         │                 │
                             \ /                │
                                                │
       ┌─────── Step 1 <─── Models              │
       │         Data        Run                │
      \ /                                       │
                                                │
    Models ───> Step 2 ───────┐                 ├ Simulation
     Run         Data         │                 │
                             \ /                │
                                                │
       ┌─────── Step 3 <─── Models              │
       │         Data        Run                │
      \ /                                       │
                                                │
    Models ───> Step 4                          ┘
     Run          │                             
                 \ /                            
                                                 
         Results are rendered                   ┐
           in the client UI                     ├ Results
            Export to CSV                       ┘


Models
------

Models define a point, connection, area or extending model as well as the map 
object that will represent it.

License
-------

The MapServer is licensed under the MIT License. See LICENSE for details.
