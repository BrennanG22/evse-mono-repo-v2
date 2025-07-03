# Stage 1: Build
FROM golang AS builder
ARG TARGETARCH
WORKDIR /app
COPY . .
RUN CGO_ENABLED=0 GOOS=linux GOARCH=${TARGETARCH} go build -o server .

# Stage 2: Runtime
FROM alpine:latest
WORKDIR /root/
COPY --from=builder /app/server .
COPY --from=builder /app/dist ./dist
EXPOSE 3000
CMD ["/root/server"]