# Implementación de Lista de Artículos en Delta Tecmi

Este plan detalla la transformación del `SecondFragment` de un mensaje de bienvenida estático a una lista funcional de productos con tendencias de precio, favoritos y navegación.

## Proposed Changes

### [Recursos y Estilos]

#### [MODIFY] [strings.xml](file:///C:/Users/deleo/AndroidStudioProjects/Actividad1Aplicacion/app/src/main/res/values/strings.xml)
Añadir nombres de productos, tiendas y etiquetas para la nueva interfaz.

#### [MODIFY] [colors.xml](file:///C:/Users/deleo/AndroidStudioProjects/Actividad1Aplicacion/app/src/main/res/values/colors.xml)
Añadir colores para las tendencias (subida/bajada).

#### [NEW] Iconos Vectoriales
Crear archivos XML para los iconos de tendencia (triángulos), estrella de favorito y engranaje de ajustes.

### [Componentes de la Lista]

#### [NEW] [Product.kt](file:///C:/Users/deleo/AndroidStudioProjects/Actividad1Aplicacion/app/src/main/java/com/example/actividad1aplicacion/Product.kt)
Clase de datos simple para representar un artículo.

#### [NEW] [item_product.xml](file:///C:/Users/deleo/AndroidStudioProjects/Actividad1Aplicacion/app/src/main/res/layout/item_product.xml)
Diseño de la fila individual con `ConstraintLayout`.

#### [NEW] [ProductAdapter.kt](file:///C:/Users/deleo/AndroidStudioProjects/Actividad1Aplicacion/app/src/main/java/com/example/actividad1aplicacion/ProductAdapter.kt)
Adaptador para manejar la lista y la interacción con la estrella de favoritos.

### [Pantalla de Destino]

#### [MODIFY] [fragment_second.xml](file:///C:/Users/deleo/AndroidStudioProjects/Actividad1Aplicacion/app/src/main/res/layout/fragment_second.xml)
Reemplazar el contenido actual con:
- Encabezado con título y botón de ajustes.
- Leyenda de tendencias.
- `RecyclerView` para la lista.
- Barra de navegación inferior personalizada.

#### [MODIFY] [SecondFragment.kt](file:///C:/Users/deleo/AndroidStudioProjects/Actividad1Aplicacion/app/src/main/java/com/example/actividad1aplicacion/SecondFragment.kt)
Configurar el `RecyclerView`, cargar la lista de datos de prueba y manejar la lógica de los botones.

## Verification Plan

### Manual Verification
1.  Iniciar sesión con un usuario registrado.
2.  Verificar que la lista se muestre correctamente con los colores y estilos definidos.
3.  Tocar la estrella de favorito en un producto y verificar que cambia de color (relleno/contorno).
4.  Probar el scroll de la lista.
