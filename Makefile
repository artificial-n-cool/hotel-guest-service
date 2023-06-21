build:
	docker-compose build
	docker-compose up  # -d
clean:
	docker stop guest-app
	docker stop mongo-db-guest
	docker rm guest-app
	docker rm mongo-db-guest
	docker rmi hotel-guest-service_guest-app