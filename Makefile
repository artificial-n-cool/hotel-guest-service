build:
	docker-compose build
	docker-compose up  # -d
clean:
	docker stop guest-app
	docker stop mongo-db
	docker rm guest-app
	docker rm mongo-db
	docker rmi guestapp_guest-app