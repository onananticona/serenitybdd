Feature: Añadir productos al carrito de compras

  Como un usuario registrado
  Quiero poder iniciar sesión en la aplicación
  Para poder añadir productos a mi carrito de compras

  @addProductCart
  Scenario Outline: El usuario añade un producto al carrito de compras
    Given El usuario está en la página de inicio de sesión de Product Store
    And El usuario ingresa su nombre de usuario y contraseña
      | <usuario> | <contraseña> |
    When El usuario selecciona un producto de una de las categorías
      | <producto> | <categoria> |
    And Añade el producto al carrito de compras
    Then El sistema debería mostrar el mensaje de confirmación "Product added."
    And El producto debería aparecer en el carrito de compras

    Examples:
      | usuario | contraseña | producto     | categoria |
      | Chava5  | Nttdata5#  | Sony vaio i7 | Laptops   |

