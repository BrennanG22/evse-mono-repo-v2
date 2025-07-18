CC = gcc
CFLAGS := -Wall -Wextra -Werror -Wreturn-type $(shell pkg-config --cflags gtk4)
LDFLAGS = `pkg-config --libs gtk4`
SRC := $(shell find src -name '*.c')
OUT = out/app

all: $(OUT)

$(OUT): $(SRC)
	$(CC) $(SRC) -o $(OUT) $(CFLAGS) $(LDFLAGS)

clean:
	rm -f $(OUT)