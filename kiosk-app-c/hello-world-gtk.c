#include <gtk/gtk.h>

#define DISPLAY_WIDTH 500
#define DISPLAY_HEIGHT 500

typedef enum {
  STATE_HOME,
  STATE_STATS,
  STATE_CHARGING,
  STATE_SETTINGS,
  NUM_APP_STATES
} AppState;

typedef struct {
  GtkWidget *window;
  GtkStack *stack;
  AppState current_state;

  GtkWidget *home_screen;
  GtkWidget *stats_screen;
  GtkWidget *charging_screen;
  GtkWidget *settings_screen;
} AppWidgets;

// Forward declaration
void switch_state(AppWidgets *app, AppState new_state);

// Button callback to switch screen
static void on_nav_button_clicked(GtkButton *button, gpointer user_data) {
  AppWidgets *app = (AppWidgets *)user_data;
  const char *label = gtk_button_get_label(button);

  if (g_strcmp0(label, "Home") == 0)
    switch_state(app, STATE_HOME);
  else if (g_strcmp0(label, "Stats") == 0)
    switch_state(app, STATE_STATS);
  else if (g_strcmp0(label, "Charging") == 0)
    switch_state(app, STATE_CHARGING);
  else if (g_strcmp0(label, "Settings") == 0)
    switch_state(app, STATE_SETTINGS);
}

// Helper to create a screen with a label and navigation buttons
GtkWidget* create_screen(const char *title, AppWidgets *app) {
  GtkWidget *vbox = gtk_box_new(GTK_ORIENTATION_VERTICAL, 20);

  GtkWidget *label = gtk_label_new(NULL);
  gtk_label_set_markup(GTK_LABEL(label), g_strdup_printf("<span size='xx-large'>%s</span>", title));
  gtk_box_append(GTK_BOX(vbox), label);

  // Navigation buttons
  GtkWidget *nav_box = gtk_box_new(GTK_ORIENTATION_HORIZONTAL, 10);
  const char *buttons[] = { "Home", "Stats", "Charging", "Settings" };
  for (int i = 0; i < 4; ++i) {
    GtkWidget *btn = gtk_button_new_with_label(buttons[i]);
    g_signal_connect(btn, "clicked", G_CALLBACK(on_nav_button_clicked), app);
    gtk_box_append(GTK_BOX(nav_box), btn);
  }

  gtk_box_append(GTK_BOX(vbox), nav_box);
  gtk_widget_set_hexpand(vbox, TRUE);
  gtk_widget_set_vexpand(vbox, TRUE);
  gtk_widget_set_halign(vbox, GTK_ALIGN_CENTER);
  gtk_widget_set_valign(vbox, GTK_ALIGN_CENTER);

  return vbox;
}

void switch_state(AppWidgets *app, AppState new_state) {
  static const char *state_ids[NUM_APP_STATES] = {
    "home", "stats", "charging", "settings"
  };

  if (new_state < NUM_APP_STATES) {
    gtk_stack_set_visible_child_name(app->stack, state_ids[new_state]);
    app->current_state = new_state;
  }
}

static void activate(GtkApplication *gtk_app, gpointer user_data) {
  AppWidgets *app = g_malloc0(sizeof(AppWidgets));
  app->current_state = STATE_HOME;

  // Create main window
  app->window = gtk_application_window_new(gtk_app);
  gtk_window_set_title(GTK_WINDOW(app->window), "EV State Machine");
  gtk_window_set_default_size(GTK_WINDOW(app->window), DISPLAY_WIDTH, DISPLAY_HEIGHT);
  gtk_window_set_resizable(GTK_WINDOW(app->window), FALSE);

  // Create the GtkStack container
  app->stack = GTK_STACK(gtk_stack_new());
  gtk_stack_set_transition_type(app->stack, GTK_STACK_TRANSITION_TYPE_SLIDE_LEFT_RIGHT);
  gtk_stack_set_transition_duration(app->stack, 250);

  // Create screens
  app->home_screen     = create_screen("Home Screen", app);
  app->stats_screen    = create_screen("Stats Screen", app);
  app->charging_screen = create_screen("Charging Screen", app);
  app->settings_screen = create_screen("Settings Screen", app);

  // Add screens to the stack with unique names
  gtk_stack_add_named(app->stack, app->home_screen, "home");
  gtk_stack_add_named(app->stack, app->stats_screen, "stats");
  gtk_stack_add_named(app->stack, app->charging_screen, "charging");
  gtk_stack_add_named(app->stack, app->settings_screen, "settings");

  // Show initial state
  gtk_window_set_child(GTK_WINDOW(app->window), GTK_WIDGET(app->stack));
  gtk_window_present(GTK_WINDOW(app->window));
  switch_state(app, STATE_HOME);
}

int main(int argc, char **argv) {
  GtkApplication *app = gtk_application_new("com.ev.sm", G_APPLICATION_DEFAULT_FLAGS);
  g_signal_connect(app, "activate", G_CALLBACK(activate), NULL);
  int status = g_application_run(G_APPLICATION(app), argc, argv);
  g_object_unref(app);
  return status;
}
