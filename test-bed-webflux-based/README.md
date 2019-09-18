Fleet Api Gateway
=================

[![Build Status](https://travis-ci.com/CabonlineTeam/cabonline-fleet-gateway-service.svg?token=z3oB9SMASpwWqqsWENph&branch=develop)](https://travis-ci.com/CabonlineTeam/cabonline-fleet-gateway-service)

The Fleet Api Gateway is the Api to manage Fleets, Transporters and Vehicles. It's intended to be consumed by the admin-web.


### Dependencies:

 - fleet-service
 - transporter-service
 - XAP security space
 - XAP party space

### Api documentation:

[Live doc](https://testapi.cabonline.com/fleet-gateway/docs/index.html)

### Build

    ./gradlew build
    
### Run

    ./gradlew bootRun

Run with profile _local_ to connect to the test environment. Remember to have the VPN set up to connect to test environment.