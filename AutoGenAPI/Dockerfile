# Use an official Python runtime as a parent image
FROM python:3.11-slim

ENV \
  # pip:
  PIP_NO_CACHE_DIR=1 \
  PIP_DISABLE_PIP_VERSION_CHECK=1 \
  PIP_DEFAULT_TIMEOUT=100 \
  # poetry:
  POETRY_VERSION=1.7.1 \
  POETRY_NO_INTERACTION=1 \
  POETRY_VIRTUALENVS_CREATE=false

# Set the working directory in the container to /app
WORKDIR /app

COPY ./poetry.lock ./pyproject.toml /app/
RUN pip install "poetry==$POETRY_VERSION" && \
    poetry install --no-dev

RUN pip install pyautogen autogenstudio

# Copy the current directory contents into the container at /app
COPY . /app

# Make port 8000 available to the world outside this container
EXPOSE 8001

# Run the command to start uWSGI
CMD ["uvicorn", "app:main", "--host", "0.0.0.0", "--port", "8001"]