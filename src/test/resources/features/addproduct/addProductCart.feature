@addProductCart
Feature: Añadir productos al carrito de compras

  Como un usuario registrado
  Quiero poder iniciar sesión en la aplicación
  Para poder añadir productos a mi carrito de compras

  Scenario Outline: El usuario añade un producto al carrito de compras
    Given Estoy en la página de inicio de sesión de Product Store
    And Ingreso mi nombre de usuario y contraseña
      | <usuario> | <contraseña> |
    And Veo el mensaje de bienvenida que contiene el nombre de usuario "<usuario>"
    When Selecciono un producto de la categoría
      | <producto> | <categoria> |
    And Lo añado al carrito de compras
    Then El sistema debería mostrar el mensaje de confirmación "Product added."
    And El producto debería aparecer en el carrito de compras

    Examples:
      | usuario | contraseña | producto     | categoria |
      | Chava5  | Nttdata5#  | Sony vaio i7 | Laptops   |

