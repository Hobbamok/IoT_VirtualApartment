import datetime

from argparse import ArgumentParser

from config_parser import ConfigLoader
from runner import ConfigRunner
from utils import UNITS

parser = ArgumentParser(
        prog='python data_gen.py',
        description="""Generates artificial data to be
                        used with the Virtual Apartment""",
        epilog='If you have a problem, please contact isaac.nunez@tum.de'
        )

parser.add_argument(
        '-d',
        '--date',
        default="2024-01-01T00:00:00Z",
        help='Defines the start date'
        )

parser.add_argument(
        '-r',
        '--refresh_rate',
        default="1m",
        help='Defines the runner resolution'
        )

parser.add_argument(
        '-f',
        '--frequency',
        default="1d",
        help='Defines how many days to run the data generator for'
        )

parser.add_argument(
        '--file',
        default="~/Downloads/",
        help="Provide the Parent Directory where the training dataset is located"
        )
args = parser.parse_args()


if __name__ == '__main__':

    count, unit = int(args.frequency[:-1]), args.frequency[-1]
    date = datetime.datetime.strptime(args.date, "%Y-%m-%dT%H:%M:%SZ")
    delta = datetime.timedelta(**{UNITS[unit]: 1})
    to_write = []
    for _ in range(count):
        run = ConfigRunner(date, args.refresh_rate, args.file)
        run.config()
        ret = run.run()
        to_write.extend(ret)
        date += delta
    with open("data.csv", "w") as file:
        file.write("timestamp,sensor_name,sensor_type,msg\n")
        for (msg, name, sensor_type) in to_write:
            print(msg, name, sensor_type)
            timestamp = msg[0].get("timestamp", None)
            file.write(f"{timestamp};{name};{sensor_type};{str(msg)[1:-1]}\n")
