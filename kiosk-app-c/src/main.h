#ifndef MAIN_H 
#define MAIN_H 

#include <gtk/gtk.h>

// Constants
#define DISPLAY_WIDTH  500
#define DISPLAY_HEIGHT 500

// Application states
typedef enum {
  STATE_HOME,
  STATE_STATS,
  STATE_CHARGING,
  STATE_SETTINGS,
  NUM_APP_STATES
} AppState;

// Struct holding pointers to important widgets
typedef struct {
  GtkWidget *window;
  GtkStack *stack;
  AppState current_state;

  GtkWidget *home_screen;
  GtkWidget *stats_screen;
  GtkWidget *charging_screen;
  GtkWidget *settings_screen;
} AppWidgets;

// Public API
void switch_state(AppWidgets *app, AppState new_state);
GtkWidget* create_screen(const char *title, AppWidgets *app);

#endif 