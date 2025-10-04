FROM python:3.10.12-slim

ENV PYTHONUNBUFFERED=1,PYTHONPATH=.,DOCKER_BUILDKIT=1


RUN python3 -m pip install  --trusted-host https://pypi.mirrors.ustc.edu.cn -i https://pypi.mirrors.ustc.edu.cn/simple/ --upgrade pip

RUN mkdir /app
WORKDIR /app
RUN pip uninstall numpy -y
RUN pip install  --trusted-host https://pypi.mirrors.ustc.edu.cn -i https://pypi.mirrors.ustc.edu.cn/simple/  -r requirements.txt