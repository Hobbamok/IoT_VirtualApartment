import datetime
import numpy as np

import utils

class FSR:

    def __init__(self, name, freq, min = 10, max = 1000, take = 110):
        self.name = name
        self.freq = freq
        self.min_water = min
        self.max_water = max
        self.avg_intake = take
        self.current = max
    
    @property
    def type(self):
        return "FSR"
    
    def config(self, min_time, max_time):
        self.min_time = datetime.datetime.strptime(min_time, "%H:%M:%S")
        self.max_time = datetime.datetime.strptime(max_time, "%H:%M:%S")

    def find_value(self, timestamp):
        date = datetime.datetime.fromtimestamp(timestamp/1000)
        if date.time() < self.min_time.time() or date.time() > self.max_time.time():
            return [], None
        count, unit = int(self.freq[:-1]), utils.UNITS[self.freq[-1]]
        if date.__getattribute__(unit[:-1]) % count == 0 and date.second == 0:
            ret = ([{"timestamp": timestamp, "weight": self.current}], self.name)
            self.current -= self.avg_intake + (np.random.randint(-20, 20))
            self.current = self.max_water if self.current < self.min_water else self.current
            return ret
        
        return [], None
