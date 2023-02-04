import requests
import json
import polyline
import urllib.parse

from locust import HttpUser, task, between, events
from random import randrange


taxi_stops = [
    (45.238548, 19.848225),   # Stajaliste na keju
    (45.243097, 19.836284),   # Stajaliste kod limanske pijace
    (45.256863, 19.844129),   # Stajaliste kod trifkovicevog trga
    (45.255055, 19.810161),   # Stajaliste na telepu
    (45.246540, 19.849282),    # Stajaliste kod velike menze
    (45.235866, 19.807387),     # Djordja Mikeša 2
    (45.247309, 19.796717),     # Andje Rankovic 2
    (45.259711, 19.809787),     # Veselina Maslese 62
    (45.261421, 19.823026),     # Jovana Hranilovica 2
    (45.265435, 19.847805),     # Bele njive 24
    (45.255521, 19.845071),     # Njegoseva 2
    (45.249241, 19.852152),     # Stevana Musica 20
    (45.242509, 19.844632),     # Boska Buhe 10A
    (45.254366, 19.861088),     # Strosmajerova 2
    (45.223481, 19.847990)      # Gajeva 2
]

vehicles = []


@events.test_start.add_listener
def on_test_start(environment, **kwargs):
    global vehicles, taxi_stops
    taxi_stops = [[lon, lat] for lat, lon in taxi_stops]
    response = requests.get("http://localhost:8443/api/vehicle/get-all") # {id: number, longitude:number, latitude: number}[ ]
    vehicles.extend(json.loads(response.text))


class QuickstartUser(HttpUser):
    host = 'http://localhost:8443'
    wait_time = between(0.5, 1)

    def on_start(self):
        self.vehicle =  vehicles.pop(0)
        self.coordinates = []
        self.ride = None
        self.get_new_coordinates()

    @task
    def update_vehicle_coordinates(self):
        if len(self.coordinates) > 0:
            new_coordinate = self.coordinates.pop(0)
            headers = {'Content-Type': 'application/json'}
            data = {
                "latitude": new_coordinate[1],
                "longitude": new_coordinate[0]
            }          
            requests.put(f"http://localhost:8443/api/ride/update-location/{self.vehicle['id']}", data=json.dumps(data), headers = headers)
            self.vehicle['longitude'] = new_coordinate[0]
            self.vehicle['latitude'] = new_coordinate[1]
            if self.ride == None:
                self.get_new_coordinates()
        elif len(self.coordinates) == 0: # and self.driving_to_taxi_stop:
            self.random_stop = taxi_stops[randrange(0, len(taxi_stops))]
            self.get_new_coordinates()

    def get_new_coordinates(self):
        #dobavi rutu kojom vozilo vozi, ruta se dobavlja samo ako je confirmed i started
        #ako je neaktivan onda ne treba da se prikazuje
        #ako je aktivan i :
        #ako ruta ne postoji self.coordinates = 0
        #ako ruta postoji i ako je confirmed, a do malopre nije postojala onda self.coordinates se trazi od trenutne lokacije vozila do prve destinacije
            # kada dodje do prve destinacije ne treba da se azuriraju koordinate
        #ako je ruta started, a bila je do prethodnog poziva confirmed onda je self.coordinates = routeLocation, u suprotnom ne azuriraj

        responseRide = requests.get(f"http://localhost:8443/api/ride/simulation-ride/{self.vehicle['id']}") #{vehicleStatus: string, ride: {status: RideStatus, destination: [longitude:number, latitude:number], routeCoordinates: String}    
        response = json.loads(responseRide.text)
        if response['vehicleStatus'] == 'INACTIVE':
            self.ride = None
            self.coordinates = []
            requests.get(f"http://localhost:8443/api/ride/remove-inactive-vehicle/{self.vehicle['id']}")
        elif response['vehicleStatus'] == 'ACTIVE':
            if response['ride']:         
                if response['ride']['status'] == 'CONFIRMED' and self.ride == None:
                    self.coordinates = []
                    departure = [self.vehicle['longitude'], self.vehicle['latitude']]
                    destination = response['ride']['destination'] #[longitude, latitude]
                    self.ride = response['ride']                   
                    responseRoute = requests.get(f'http://router.project-osrm.org/route/v1/car/{departure[0]},{departure[1]};{destination[0]},{destination[1]}?geometries=geojson&overview=full')
                    routeGeoJSON = json.loads(responseRoute.text)              
                    self.coordinates = [*routeGeoJSON['routes'][0]['geometry']['coordinates']]
                elif response['ride']['status'] == 'STARTED' and (self.ride == None or self.ride['status'] == 'CONFIRMED'):                    
                    self.coordinates = []
                    self.ride = response['ride']
                    route = urllib.parse.unquote(response['ride']['routeCoordinates'])
                    # [[lat, lon], []]
                    route = polyline.decode(route)
                    route = [[lon, lat] for lat, lon in route]                   
                    self.coordinates = [*self.coordinates, *route]
                elif response['ride']['status'] == 'SCHEDULED':
                    self.random_stop = taxi_stops[randrange(0, len(taxi_stops))]
                    departure = [self.vehicle['longitude'], self.vehicle['latitude']]
                    responseRouteRand = responseRoute = requests.get(f'http://router.project-osrm.org/route/v1/car/{departure[0]},{departure[1]};{self.random_stop[0]},{self.random_stop[1]}?geometries=geojson&overview=full')
                    routeGeoJSON = json.loads(responseRouteRand.text)
                    coordinateSwitch = routeGeoJSON['routes'][0]['geometry']['coordinates']
                    self.coordinates = [*self.coordinates, *coordinateSwitch]
                    self.ride = None

            else:
                self.random_stop = taxi_stops[randrange(0, len(taxi_stops))]
                departure = [self.vehicle['longitude'], self.vehicle['latitude']]
                responseRouteRand = responseRoute = requests.get(f'http://router.project-osrm.org/route/v1/car/{departure[0]},{departure[1]};{self.random_stop[0]},{self.random_stop[1]}?geometries=geojson&overview=full')
                routeGeoJSON = json.loads(responseRouteRand.text)
                coordinateSwitch = routeGeoJSON['routes'][0]['geometry']['coordinates']
                self.coordinates = [*self.coordinates, *coordinateSwitch]
                self.ride = None