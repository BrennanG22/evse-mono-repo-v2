package main

import (
	"log"

	"github.com/gotk3/gotk3/gtk"
)

func main() {
	gtk.Init(nil)
	fmt.Println("Here")

	// Create a new window
	win, err := gtk.WindowNew(gtk.WINDOW_TOPLEVEL)
	if err != nil {
		log.Fatal("Unable to create window:", err)
	}
	win.SetTitle("GTK Go Example")
	win.SetDefaultSize(400, 200)

	// Create a button and connect a signal
	btn, err := gtk.ButtonNewWithLabel("Click Me")
	if err != nil {
		log.Fatal("Unable to create button:", err)
	}
	btn.Connect("clicked", func() {
		log.Println("Button clicked!")
	})

	win.Add(btn)

	win.Connect("destroy", func() {
		gtk.MainQuit()
	})

	win.ShowAll()

	gtk.Main()
}