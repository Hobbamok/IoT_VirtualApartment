import datetime

import pandas as pd
import numpy as np
import utils

class DHT:

    def __init__(self, name, start_date):
        self.name = name
        self.start_date = start_date #.datetime.strptime(start_date, "%Y-%m-%d")
        self.values = []
        self.sorted = False

    @property
    def type(self):
        return "Temperature"

    def config(self, time_start, time_end, freq, temp_range, hum_range):
        time_start = datetime.datetime.strptime(time_start, "%H:%M:%S").time()
        time_end = datetime.datetime.strptime(time_end, "%H:%M:%S").time()
        curr_time = datetime.datetime.combine(self.start_date, time_start)
        count, unit = int(freq[:-1]), freq[-1]
        delta = datetime.timedelta(**{utils.UNITS[unit]: count})
        future_delta = datetime.timedelta(**{"days": 1 if time_end < time_start else 0})
        tmp_dates = []
        if time_end < time_start:
            curr_time = datetime.datetime.combine(self.start_date, time_start) - future_delta
            future_time = datetime.datetime.combine(self.start_date, time_end)
            while (curr_time < future_time):
                tmp_dates.append(curr_time)
                curr_time += delta
            curr_time = datetime.datetime.combine(self.start_date, time_start)
            future_time = datetime.datetime.combine(self.start_date, time_end) + future_delta
            while(curr_time < future_time):
                tmp_dates.append(curr_time)
                curr_time += delta
        else:
            future_time = datetime.datetime.combine(self.start_date, time_end) + future_delta
            while (future_time > curr_time):
                tmp_dates.append(curr_time)
                curr_time += delta
        min_hum, max_hum = hum_range
        min_temp, max_temp = temp_range
        mean_temp = np.mean(temp_range)
        mean_hum = np.mean(hum_range)
        temp_gauss = np.random.normal(0, 0.1, len(tmp_dates))
        hum_guass = np.random.normal(0, 0.2, len(tmp_dates))

        def return_inside_interval(val, max, min):
            if val < min:
                return min
            if val > max:
                return max
            return val
        for date, tdx, hdx in zip(tmp_dates, temp_gauss, hum_guass):
            res_temp = mean_temp*tdx + (max_temp if tdx < 0 else min_temp)
            res_hum = mean_hum*hdx + (max_hum if hdx < 0 else min_hum)
            self.values.append({
                "timestamp": int(date.timestamp()*1000),
                "temperature": round(return_inside_interval(res_temp, max_temp, min_temp), 2),
                "humidity": round(return_inside_interval(res_hum, max_hum, min_hum), 2)
                })

    def print(self):
        if not self.sorted:
            self.df = pd.DataFrame(self.values, columns=["timestamp", "temperature", "humidity"])
            self.df.sort_values("timestamp", inplace=True)
            self.sorted = True

        print(self.df)

    def find_value(self, timestamp):
        if not self.sorted:
            self.df = pd.DataFrame(self.values, columns=["timestamp", "temperature", "humidity"])
            self.df.sort_values("timestamp", inplace=True)
            self.sorted = True
        return self.df.loc[self.df["timestamp"].eq(timestamp)].to_dict("records"), self.name
