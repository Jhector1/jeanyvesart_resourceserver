# jeanyvesart_resourceserver
---
title: Domain Model Overview
---
classDiagram
direction LR

class MyProduct {
<<Entity (SINGLE_TABLE)>>
+long id
+String imageUrl
+String title
+String price
+String description
+int quantity
}

class Artwork {
<<Entity>>
+String medium
+String size
}
MyProduct <|-- Artwork

class MyReview {
<<Entity>>
+long id
+String headline
+String reviewText
+int rating
+Date date
+byte[] imageData
+String imageName
}
MyProduct "1" --> "0..*" MyReview : reviews
MyReview  "*" --> "1" MyProduct  : product
MyReview  "*" --> "1" MyCustomer : author

class MyCustomer {
<<Entity>>
+String id
+String fullName
+String email
+String telephone
+String authority
+String password
+String resetToken
+Date   resetTokenDate
+boolean resetTokenUsed
+Date   expirationDate
+boolean collector
}

class Address {
<<Entity>>
+Long id
+String country
+String street
+String city
+String state
+String zip
+String apartment
}

MyCustomer "1" --> "0..*" Address : addressList

%% Orders
class MyOrder {
<<Entity>>
+long id
+Date date
}
MyCustomer "1" --> "0..*" MyOrder : orders
MyOrder   "*" --> "*" MyProduct   : products

%% Checkout (cart/transaction snapshot)
class Checkout {
<<Entity>>
+Long id
+Date placedAt
}
Checkout "1" --> "1" Address    : billingAddress
Checkout "1" --> "1" MyCustomer : buyer
Checkout "1" --> "0..*" MyProduct : artworks

%% CustomerData hierarchy (Favorites, Cart)
class CustomerData~H~ {
<<MappedSuperclass>>
+long id
+Date date
}
class CustomerDataHelper {
<<MappedSuperclass>>
+long id
+int quantity
+Date date
}
CustomerData "1" --> "0..*" CustomerDataHelper : helpers
CustomerData "1" --> "1"   MyCustomer          : myCustomer
CustomerDataHelper "*" --> "1" MyProduct        : myProduct

class CustomerFavorite {
<<Entity>>
}
class CustomerFavoriteHelper {
<<Entity>>
}
CustomerData <|-- CustomerFavorite
CustomerDataHelper <|-- CustomerFavoriteHelper
MyCustomer "1" --> "0..1" CustomerFavorite : favorite

class CustomerCart {
<<Entity>>
}
class CustomerCartHelper {
<<Entity>>
}
CustomerData <|-- CustomerCart
CustomerDataHelper <|-- CustomerCartHelper
MyCustomer "1" --> "0..1" CustomerCart : cart

%% Inventory
class DepotInventory {
<<Entity>>
+Long id
+String category
}
class Inventory {
<<Entity>>
+String id
+String category
+int quantity
+List~String~ metadata
}
DepotInventory "1" --> "0..*" Inventory : inventories
Inventory "1" --> "1" MyProduct : myProduct
