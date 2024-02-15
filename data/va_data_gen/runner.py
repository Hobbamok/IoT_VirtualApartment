import os
import datetime


from dht import DHT
from camera import CAMERA
from fsr import FSR
from pir import PIR
from hr import HR
from utils import UNITS


class ConfigRunner:

    def __init__(self, start_date: datetime, refresh_rate: str = "1s", data_path = "~/Downloads/"):
        self.refresh_rate = refresh_rate
        self.start_date = start_date
        self.parent_path = data_path
        self.sensors = []
        self.hr = None

    def __new__(cls, start_date: datetime, refresh_rate: str = "1s", data_path = "~/Downloads/"):
        return super(ConfigRunner, cls).__new__(cls)

    def config(self):
        k_dht = DHT("K_DHT", self.start_date.date())
        lr_dht = DHT("LR_DHT", self.start_date.date())
        bath_dht = DHT("BATH_DHT", self.start_date.date())
        b_dht = DHT("B_DHT", self.start_date.date())
        pir = PIR(self.start_date.date())
        k_camera = CAMERA("K_CAMERA", "1h")
        lr_camera = CAMERA("LR_CAMERA", "45m")
        self.hr = HR("HR", self.start_date.date(), [65, 85])
        fsr = FSR("FSR", "20m")
        self.sensors = [k_camera, k_dht, lr_camera, lr_dht, bath_dht, b_dht, pir, fsr]

        k_dht.config("19:00:00", "8:59:59", "20m", [20, 22], [30, 40])
        k_dht.config("9:00:00", "9:59:59", "20m", [22, 26], [40, 60])
        k_dht.config("10:00:00", "11:59:59", "20m", [20, 22], [30, 34])
        k_dht.config("12:00:00", "12:59:59", "20m", [24, 27], [50, 80])
        k_dht.config("13:00:00", "16:59:59", "20m", [20, 22], [30, 45])
        k_dht.config("17:00:00", "18:59:59", "20m", [23, 28], [50, 78])

        bath_dht.config("21:00:00", "6:59:59", "20m", [20, 22], [40, 60])
        bath_dht.config("7:00:00", "20:59:59", "20m", [23, 26], [55, 75])

        b_dht.config("20:00:00", "7:59:59", "20m", [20, 22], [40, 60])
        b_dht.config("8:00:00", "11:59:59", "20m", [21, 24], [30, 45])
        b_dht.config("12:00:00", "19:59:59", "20m", [20, 23], [45, 70])

        lr_dht.config("20:00:00", "7:59:59", "20m", [19, 21], [30, 50])
        lr_dht.config("8:00:00", "11:59:59", "20m", [21, 25], [35, 47])
        lr_dht.config("12:00:00", "19:59:59", "20m", [19, 26], [43, 68])
        if "~" in self.parent_path:
            dataset_path = os.path.join(os.path.expanduser(self.parent_path), "train/")
        else:
            dataset_path = os.path.join(os.path.abspath(self.parent_path), "train/")
        if os.path.exists(dataset_path):
            k_camera.config(os.path.join(dataset_path, "split1/"))
            k_camera.config(os.path.join(dataset_path, "split2/"))

            lr_camera.config(os.path.join(dataset_path, "split3/"))
            lr_camera.config(os.path.join(dataset_path, "split10/"))
        else:
            raise ValueError(f"""
            The training dataset is missing on [{dataset_path}].
            Please download it from:
                https://gram.web.uah.es/data/datasets/fpds/index.html
            You should use the E-FPDS (Training set) folder.
                             """)
        pir.config()
        fsr.config("8:00:00", "22:00:00")

    def run(self):
        if self.start_date is None:
            return []
        ret = []
        current_time = self.start_date
        unit = UNITS.get(self.refresh_rate[-1], None)
        if unit is None:
            raise KeyError(
                    f"The refresh_rate[{self.refresh_rate}] cannot be parsed"
            )
        count = int(self.refresh_rate[:-1])
        delta = datetime.timedelta(**{unit: count})
        while (current_time.date() <= self.start_date.date()):
            for sensor in self.sensors:
                try:
                    msg, name = sensor.find_value(int(current_time.timestamp()*1000))
                    if len(msg) > 0:
                        ret.append((msg, name, sensor.type))
                        print(f"{current_time} ----- [{name}]->{msg}")
                        if name == "BATH_PIR":
                            msg, name = self.hr.find_value(int(current_time.timestamp()*1000))
                            ret.append((msg, name, self.hr.type))
                            print(f"[{name}]->{msg}")
                except TypeError as e:
                    print(f"{sensor.name} failed to execute")
                    print(e)
            current_time += delta
        return ret
