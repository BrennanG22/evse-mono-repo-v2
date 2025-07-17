#include "home_screen.h"

static void on_click(GtkGestureClick *gesture G_GNUC_UNUSED,
                     int n_press G_GNUC_UNUSED,
                     double x,
                     double y,
                     gpointer user_data G_GNUC_UNUSED)
{
  g_print("Clicked at (%.0f, %.0f)\n", x, y);
}

GtkWidget *get_home_screen()
{
  GtkWidget *vbox = gtk_box_new(GTK_ORIENTATION_VERTICAL, 0);
  GtkGesture *click = gtk_gesture_click_new();
  gtk_widget_add_controller(vbox, GTK_EVENT_CONTROLLER(click));
  g_signal_connect(click, "pressed", G_CALLBACK(on_click), NULL);
  return vbox;
}
