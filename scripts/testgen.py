#!/usr/bin/env python2
# -*- coding: utf-8 -*-

import csv
import os
import subprocess
import argparse


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("template", nargs="?", type=argparse.FileType("r"),
                        default="in/testcase.csv.template",
                        help="template file")
    parser.add_argument("data", nargs="?", type=argparse.FileType("r"),
                        default="in/testcase.csv.data",
                        help="data file to populate the template with")
    parser.add_argument("-s", "--skip", nargs=1, type=int,
                        help="how many lines to skip in data", default=[0])
    args = parser.parse_args()

    try:
        os.mkdir("out")
    except OSError:
        pass

    template = None
    with args.template as fp:
        template = fp.read()
        fp.close()

    with args.data as fp:
        reader = csv.reader(fp, delimiter=",")
        for inx, row in enumerate(reader):
            if inx >= args.skip[0]:
                data = template

                for inx2, vari in enumerate(row):
                    data = data.replace("$" + str(inx2), vari)

                fname = os.path.join("out", "testcase_" + str(inx) + ".csv")

                with open(fname, "w") as fp2:
                    fp2.write(data)
                    fp2.close()
                print "Saved " + fname

        fp.close()


if __name__ == "__main__":
    main()
