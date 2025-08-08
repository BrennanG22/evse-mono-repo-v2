FROM golang AS builder
ARG TARGETARCH
ARG TARGETVARIANT

WORKDIR /app
COPY . .

# Set GOARM based on TARGETVARIANT (default to 7 if unspecified)
RUN if [ "$TARGETARCH" = "arm" ]; then \
  if [ "$TARGETVARIANT" = "v7" ]; then export GOARM=7; \
  elif [ "$TARGETVARIANT" = "v6" ]; then export GOARM=6; \
  else export GOARM=7; fi; \
  fi && \
  CGO_ENABLED=0 GOOS=linux GOARCH=$TARGETARCH go build -o server .

# Stage 2: Runtime
FROM alpine:latest
WORKDIR /root/
COPY --from=builder /app/server .
COPY --from=builder /app/dist ./dist
CMD ["/root/server"]