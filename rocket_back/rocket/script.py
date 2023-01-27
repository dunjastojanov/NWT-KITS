import argparse
import subprocess

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("-u", type=int, help="Number of users to simulate")
    parser.add_argument("-r", type=int, help="Number of requests per second")
    parser.add_argument("--run-time", type=str, help="Duration of the simulation (ex: 30m)")
    args = parser.parse_args()

    subprocess.call("D:\siit\4. god\NWT\NWT-KITS\vehicle-simulation\Scripts\activate", shell=True)
    subprocess.call("locust -f simulation.py --headless -u {} -r {} --run-time {}".format(args.u, args.r, args.run_time), shell=True)

if __name__ == "__main__":
    main()