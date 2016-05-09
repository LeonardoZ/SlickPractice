package com.leonardoz.starred

import org.h2.security.SHA256
import scala.concurrent.ExecutionContext.Implicits.global
import slick.driver.H2Driver.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main extends App {

  val db = Database.forConfig("h2mem1")

  val tempId = -999

  def passwordToHash(password: String): String = new String(SHA256.getHash(password.getBytes("UTF-8"), false)).trim

  // Users
  val users = TableQuery[UserTable]

  val createUsersTable = users.schema.create

  val usersData = Seq(
    User(tempId, "leo@gmail.com", passwordToHash("kasj2399aaa")),
    User(tempId, "loid@gmail.com", passwordToHash("98120937204lhaflas")),
    User(tempId, "lorn@gmail.com", passwordToHash("klaldlajsl!@!s")),
    User(tempId, "carl.banksTop.crosssbanest@gmail.com", passwordToHash("lkçoannnn"))
  )

  val addUsers = users ++= usersData;

  val selectUsers = users.result

  val resultIs: Unit = Await.result(db.run(DBIO.seq(createUsersTable, addUsers)), Duration.Inf)

  val usersFound = Await.result(db.run(selectUsers), Duration.Inf)

  println(s"Users found: \n $usersFound")


  // Movies
  val moviesData = Seq(
    Movie(tempId, "Movie 1", 1993),
    Movie(tempId, "Movie 2", 2005),
    Movie(tempId, "Movie 3", 2016),
    Movie(tempId, "Movie 4", 1967),
    Movie(tempId, "Movie 5", 1977),
    Movie(tempId, "Movie 6", 1993))

  val movies = TableQuery[MovieTable]

  val createMovieTable = movies.schema.create

  val addMovies = movies ++= moviesData

  val selectMovies = movies.result

  val resultMoviesIs: Unit = Await.result(db.run(DBIO.seq(createMovieTable, addMovies)), Duration.Inf)

  val moviesFound = Await.result(db.run(selectMovies), Duration.Inf)

  println(s"Movies found: \n$moviesFound")


  // Classifications

  val moviesAndUsers: Seq[(User, Movie)] = usersFound zip moviesFound

  val classificationsData: Seq[Classification] = moviesAndUsers.map(tu => Classification(tempId, 2, tu._2.id, tu._1.id))

  val classifications = TableQuery[ClassificationTable]

  val createClassificationTable = classifications.schema.create

  val addClassification = classifications ++= classificationsData

  val selectClassifications = classifications.result

  val resultClassificationsIs: Unit = Await.result(db.run(DBIO.seq(createClassificationTable, addClassification)), Duration.Inf)

  val classificationsFound = Await.result(db.run(selectClassifications), Duration.Inf)

  println(s"Classifications found: \n$classificationsFound")

  val classificationJoinMovie: Query[(ClassificationTable, Rep[Option[MovieTable]]), (Classification, Option[Movie]), Seq] =
    for {
      (mov, cla) <- classifications joinLeft movies on  (_.movieId === _.id)
    } yield (mov, cla)



  // Print the rows which contain the coffee name and the supplier name
  db.run(classificationJoinMovie.result).map(println)

  db.close()

  // query data


}
