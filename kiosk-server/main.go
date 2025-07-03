package main

import (
	"log"
	"net/http"
)

func main() {
	fs := http.FileServer(http.Dir("./dist"))

	// Serve static files
	http.Handle("/", fs)

	port := ":3000"
	log.Printf("Server running at http://localhost%s", port)
	log.Fatal(http.ListenAndServe(port, nil))
}