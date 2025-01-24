FROM alpine
LABEL authors="soccerlover9527@gmail.com"
COPY implement/target/wx-assistant-plugin-implement /usr/local/bin/wx-assistant-plugin-implement

ENTRYPOINT ["/usr/local/bin/wx-assistant-plugin-implement"]