import cats.effect._
import org.flywaydb.core.Flyway

object Main extends IOApp {

    def run(args: List[String]): IO[ExitCode] = {
        for {
            _ ← migrate[IO]
        } yield ExitCode.Success
    }

    def migrate[F[_]](implicit F: Sync[F]): F[Unit] = F.delay {
      val flyway = new Flyway()
      flyway.setDataSource("jdbc:postgresql:postgres", "postgres", "")
      flyway.migrate()
    }

}
