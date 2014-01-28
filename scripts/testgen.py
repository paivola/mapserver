#!/usr/bin/env python2
# -*- coding: utf-8 -*-

import csv
import os
import subprocess


def main():
    try:
        os.mkdir("out")
    except OSError:
        pass

    template = None
    with open(os.path.join("in", "testcase.csv.template"), "r") as fp:
        template = fp.read()
        fp.close()

    with open(os.path.join("in", "testcase.csv.data"), "rb") as fp:
        reader = csv.reader(fp, delimiter=",")
        for inx, row in enumerate(reader):
            data = template

            for inx2, vari in enumerate(row):
                data = data.replace("$" + str(inx2), vari)

            with open(os.path.join("out", "testcase_" + str(inx) + ".csv"), "w") as fp2:
                fp2.write(data)
                fp2.close()
            print "Saved " + os.path.join("out", "testcase_" + str(inx) + ".csv")

        fp.close()


if __name__ == "__main__":
    main()
