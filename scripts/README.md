testgen.py
==========

uses `in/testcase.csv.template` and `in/testcase.csv.data` to generate stuff into `out`

plotscript.m
============

Usage
This script makes .png-file of one .csv-file or all .csv-files in the same directory. 

Making one .png-file:

        Write into the command line

		$ octave plotscript.m input.csv

Making .png-files of all .csv-files in the same directory:
	
	Write into the command line
		
		$ for i in *.csv;do octave plotscript.m "$i";done

.png-files will be named: input.csv.png

NOTE
------
* Coded with Octave 3.2.4 (because there isn't newer versions for ubuntu), it may 
or may not work with other versions. 
* Assumes that the first line is uncommented text and no others contain it.

Dependencies
* GNU Octave
* gnuplot
