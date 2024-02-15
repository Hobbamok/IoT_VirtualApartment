import datetime
import pandas as pd

import utils

class PIR:
    def __init__(self, start_date, duration_change="1m", time_delta = 1):
        self.start_date = start_date #datetime.datetime.strptime(start_date, "%Y-%m-%d")
        count, unit = int(duration_change[:-1]), duration_change[-1]
        self.delay = datetime.timedelta(**{utils.UNITS[unit]: count})
        self.delta = time_delta
        self.values = []
        self.sorted = False

    @property
    def type(self):
        return "PIR"

    def config(self, filename = "movement.csv"):
        df = pd.read_csv(filename)
        for index, row in df.iterrows():
            time = datetime.datetime.strptime(row["time"], "%H:%M:%S").time()
            timestamp = datetime.datetime.combine(self.start_date, time)
            name = row["name"]
            value = row["value"]
            self.values.append({
                "timestamp": int(timestamp.timestamp()*1000),
                "presence": value,
                "name": name
                })

    def find_value(self, timestamp):
        if not self.sorted:
            self.df = pd.DataFrame(self.values, columns=["timestamp", "presence", "name"])
            self.df.sort_values("timestamp", inplace=True)
            self.sorted = True
        result = self.df.loc[self.df["timestamp"].eq(timestamp)].to_dict("records")
        if len(result) == 0:
            return [], None
        result = result[0]
        name = result.pop("name", None)
        return [result], name
