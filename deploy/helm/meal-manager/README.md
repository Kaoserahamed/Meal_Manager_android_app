Chart.yaml:
apiVersion: v2
name: meal-manager
description: Meal Manager backend Helm chart
type: application
version: 1.0.0
appVersion: 1.0.0

values.yaml:
replicaCount: 3
image:
  repository: mealmanager/backend
  pullPolicy: IfNotPresent
  tag: latest
service:
  type: LoadBalancer
  port: 80
  targetPort: 8080
resources:
  limits:
    cpu: 500m
    memory: 512Mi
  requests:
    cpu: 250m
    memory: 256Mi
autoscaling:
  enabled: true
  minReplicas: 2
  maxReplicas: 10
  targetCPUUtilizationPercentage: 70

templates/_helpers.tpl:
{{- define "meal-manager.fullname" -}}
{{- printf "%s-%s" .Release.Name "backend" -}}
{{- end }}

templates/deployment.yaml:
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ include "meal-manager.fullname" . }}
  labels:
    app: {{ include "meal-manager.fullname" . }}
spec:
  replicas: {{ .Values.replicaCount }}
  selector:
    matchLabels:
      app: {{ include "meal-manager.fullname" . }}
  template:
    metadata:
      labels:
        app: {{ include "meal-manager.fullname" . }}
    spec:
      containers:
      - name: backend
        image: {{ .Values.image.repository }}:{{ .Values.image.tag }}
        ports:
        - containerPort: 8080
        resources:
          limits:
            cpu: {{ .Values.resources.limits.cpu }}
            memory: {{ .Values.resources.limits.memory }}
          requests:
            cpu: {{ .Values.resources.requests.cpu }}
            memory: {{ .Values.resources.requests.memory }}

templates/service.yaml:
apiVersion: v1
kind: Service
metadata:
  name: {{ include "meal-manager.fullname" . }}
spec:
  selector:
    app: {{ include "meal-manager.fullname" . }}
  ports:
  - protocol: TCP
    port: {{ .Values.service.port }}
    targetPort: {{ .Values.service.targetPort }}
  type: {{ .Values.service.type }}
