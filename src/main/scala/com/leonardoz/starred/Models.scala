package com.leonardoz.starred

import slick.driver.H2Driver.api._
import slick.lifted.{ProvenShape, Tag}


case class User(id: Long, email: String, password: String)

class UserTable(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Long]("user_id", O.PrimaryKey, O.AutoInc)

  def email = column[String]("email", O.Length(150))

  def password = column[String]("password", O.Length(64))

  def * : ProvenShape[User] = (id, email, password) <> (User.tupled, User.unapply)
}

case class Movie(id: Long, title: String, releaseYear: Int)

class MovieTable(tag: Tag) extends Table[Movie](tag, "movies") {
  def id = column[Long]("movie_id", O.PrimaryKey, O.AutoInc)

  def title = column[String]("title", O.Length(200))

  def releaseYear = column[Int]("release_year")

  def * : ProvenShape[Movie] = (id, title, releaseYear) <> (Movie.tupled, Movie.unapply)
}

case class Classification(id: Long, score: Int, movieId: Long, userId: Long)

class ClassificationTable(tag: Tag) extends Table[Classification](tag, "classification") {
  def id = column[Long]("classification_id", O.PrimaryKey, O.AutoInc)

  def score = column[Int]("score")

  def movieId = column[Long]("movie_id")

  def userId = column[Long]("user_id")

  def movie = foreignKey("fk_classification_movie", movieId, TableQuery[MovieTable])(_.id)

  def user = foreignKey("fk_classification_user", movieId, TableQuery[UserTable])(_.id)

  def * : ProvenShape[Classification] = (id, score, movieId, userId) <> (Classification.tupled, Classification.unapply)

}