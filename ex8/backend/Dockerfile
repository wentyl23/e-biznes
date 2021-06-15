FROM ubuntu:18.04

#docker build . -t wentyl23/e-biznes:latest
#docker push wentyl23/e-biznes:latest

# base tools
RUN apt-get update && \
    apt-get install -y \
    git \
    wget \
    curl \
    gnupg \
    unzip

# Install OpenJDK-8
RUN apt-get update &&\
    apt-get install -y openjdk-8-jdk &&\
    apt-get clean

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH

# Install scala 2.12
RUN wget https://downloads.lightbend.com/scala/2.12.3/scala-2.12.3.deb && \
    dpkg -i scala-2.12.3.deb && \
    apt-get update && \
    apt-get install -y scala

# Install SBT
# https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html
RUN echo "deb http://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list &&\
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add &&\
    apt-get update &&\
    apt-get install -y sbt

RUN adduser --disabled-password \
    --gecos '' wentyl23
RUN adduser wentyl23 sudo
RUN echo '%sudo ALL=(ALL) NOPASSWD:ALL' >> \
    /etc/sudoers
USER wentyl23
WORKDIR /home/wentyl23/

RUN cd /home/wentyl23/
COPY . .

# CMD sbt "run 8080"
# CMD sbt "start -Dhttp.port=8080"
RUN sbt playUpdateSecret
RUN sbt stage
CMD cd target/universal/stage/bin && chmod +x ex2 && ./ex2 -Dhttp.port=9000
# CMD target/universal/stage/bin/sklep-play -Dhttp.port=9000