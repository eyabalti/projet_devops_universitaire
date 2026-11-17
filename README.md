# Gestion universitaire – Projet Ops DevOps

Application REST Spring Boot de gestion des étudiants, packagée et exploitée par Docker Compose.

## Prérequis
- Docker Desktop (Windows/macOS) ou Docker Engine + Compose
- Git
- Java 21 et Maven (facultatifs si compilation uniquement dans Docker)
- Jenkins avec accès au Docker daemon (pour la partie pipeline)

## Démarrage rapide
1. Copier `.env.example` vers `.env`, puis modifier les mots de passe.
2. Construire et lancer :
   ```bash
   docker compose up -d --build
   docker compose ps
   docker compose logs -f app
   ```
3. Tester :
   - API via Nginx : `http://localhost/api/etudiants`
   - Health : `http://localhost/actuator/health`
   - Prometheus : `http://localhost:9090`
   - Grafana : `http://localhost:3000` (admin + mot de passe `.env`)

## API
- `GET /api/etudiants` : liste
- `GET /api/etudiants/{id}` : détail
- `POST /api/etudiants` : création
- `PUT /api/etudiants/{id}` : modification
- `DELETE /api/etudiants/{id}` : suppression

Exemple :
```bash
curl -X POST http://localhost/api/etudiants -H "Content-Type: application/json" -d '{"nom":"Ben Ali","prenom":"Amine","email":"amine@example.com"}'
curl http://localhost/api/etudiants
```

## Monitoring
Prometheus collecte les métriques Spring Boot sur `app:8080/actuator/prometheus`.
Grafana est provisionné avec la datasource Prometheus. Ajouter un dashboard (ex. JVM/Micrometer) et visualiser :
- `jvm_memory_used_bytes`
- `process_cpu_usage`
- `http_server_requests_seconds_count`
- `system_cpu_usage`

## Jenkins
- Installer les plugins Pipeline, Git et Docker Pipeline (ou utiliser `docker` CLI sur agent).
- Configurer un agent ayant Docker et Maven disponibles.
- Créer un Pipeline « from SCM » pointant sur le dépôt GitHub.
- La stage Deploy est conditionnée à la branche `main`.
- En production, utiliser credentials Jenkins, registry, SSH/agent dédié et stratégie de rollback; ne pas stocker les secrets dans le dépôt.

## Scénarios de panne (TP)
1. **Application arrêtée** : `docker compose stop app`; observer l'erreur HTTP côté Nginx et l'absence de scrape Prometheus. Restaurer : `docker compose start app`.
2. **MySQL arrêté** : `docker compose stop mysql`; consulter `docker compose logs app`, constater les erreurs de connexion. Restaurer : `docker compose start mysql`, puis attendre le healthcheck.
3. **Nginx arrêté** : `docker compose stop nginx`; le client ne peut plus accéder au service via le port 80. Restaurer : `docker compose start nginx`.
4. **Configuration incorrecte** : modifier temporairement la cible Prometheus, puis observer `UP=0`; remettre la configuration et redémarrer Prometheus.

## Commandes d'exploitation
```bash
docker compose ps
docker compose logs --tail=100 app
docker compose exec mysql mysql -u universite -p universite
docker compose restart app
docker compose down                 # arrête et supprime les conteneurs (volumes conservés)
docker compose down -v              # ATTENTION : supprime aussi les données persistantes
docker compose up -d --build
```

## Sauvegarde / restauration MySQL
```bash
docker compose exec -T mysql sh -c 'mysqldump -uuniversite -p"$MYSQL_PASSWORD" universite' > backup.sql
cat backup.sql | docker compose exec -T mysql sh -c 'mysql -uuniversite -p"$MYSQL_PASSWORD" universite'
```
Pour le TP, vérifier la sauvegarde avant toute restauration. Les mots de passe et données doivent être traités comme secrets.

## Rapport d'exploitation (à remettre)
1. Architecture et versions des composants
2. Procédure de déploiement et preuve (pipeline/logs)
3. Tableau de bord et métriques observées
4. Pour chaque incident : symptôme, hypothèse, diagnostic (commandes/logs), cause, correction, validation
5. Sauvegarde/restauration : procédure et résultat
6. Bilan : disponibilité, limites et recommandations
