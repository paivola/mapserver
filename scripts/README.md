Usage
------

<<<<<<< HEAD
This script makes .png-file of one .csv-file or all .csv-files in the same directory. 

Making one .png-file:

        Write into the command line

		$ octave plotscript.m input.csv

Making .png-files of all .csv-files in the same directory:
	
	Write into the command line
		
		$ for i in *.csv;do octave plotscript.m "$i";done

.png-files will be named: input.csv.png

		
=======
Write into the command line

	$ octave plotscript.m input.csv

and the file output.png will be generated with all your data in it.


* OCTAVE
* GNUPLOT
=======
* GNU Octave
* gnuplot
