#include "home_screen.h"

#define U_OF_A_LOGO_PATH "assets/university-of-alberta-logo.png"

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
  gtk_widget_set_name(vbox, "main-box");

  GtkWidget *image_box = gtk_box_new(GTK_ORIENTATION_VERTICAL, 0);
  gtk_widget_set_size_request(image_box, 300, 150);
  GtkWidget *picture = gtk_picture_new_for_filename(U_OF_A_LOGO_PATH);
  gtk_widget_set_size_request(picture, 300, 150);
  gtk_widget_set_hexpand(picture, TRUE);
  gtk_box_append(GTK_BOX(image_box), picture);
  gtk_box_append(GTK_BOX(vbox), image_box);

  gtk_widget_set_margin_start(picture, 10);
  gtk_widget_set_margin_end(picture, 10);
  gtk_widget_set_margin_top(picture, 10);


  GtkWidget *start_label = gtk_label_new("Touch anywhere\n to start");
  gtk_box_append(GTK_BOX(vbox), start_label);

  GtkGesture *click = gtk_gesture_click_new();
  gtk_widget_add_controller(vbox, GTK_EVENT_CONTROLLER(click));
  g_signal_connect(click, "pressed", G_CALLBACK(on_click), NULL);
  return vbox;
}
