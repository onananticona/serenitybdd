Feature: Iniciar sesión en Product Store

  Como un usuario registrado
  Quiero poder iniciar sesión en la aplicación
  Para poder acceder a mi cuenta y gestionar mis datos

  @login
  Scenario Outline: El usuario inicia sesión con credenciales válidas
    Given El usuario está en la página de inicio de sesión de Product Store
    When El usuario ingresa su nombre de usuario y contraseña
      | <usuario> | <contraseña> |
    Then El usuario debería ver un mensaje de bienvenida que contiene su nombre de usuario

    Examples:
      | usuario     | contraseña |
      | Chava5      | Nttdata5#  |
      | nicobarella | Nttdata1#  |

#  Scenario Outline: El usuario inicia sesión con credenciales inválidas
#    Given Estoy en la página de inicio de sesión de Product Store
#    When Ingreso mi nombre de usuario y contraseña
#      | <usuario> | <contraseña> |
#    Then Debería ver un mensaje de error que diga "Wrong password."
#
#    Examples:
#      | usuario      | contraseña |
#      | Chava5A      | Nttdata5#  |
#      | LukitaA      | poni#19    |
#      | nicobarellaA | Nttdata1#  |
#
