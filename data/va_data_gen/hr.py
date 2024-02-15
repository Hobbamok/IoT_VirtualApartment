

import numpy as np

class HR:

    def __init__(self, name, start_date, hr_range, run_anomaly=False):
        self.name = name
        self.hr_range = hr_range
        self.anomaly = run_anomaly
        self.callbacks = []

    @property
    def type(self):
        return "HR"

    def find_value(self, timestamp):
        rng = self.hr_range if not self.anomaly else [120, 170]
        return [{
                "timestamp": timestamp,
                "hr": np.random.randint(*rng)
                }], self.name
