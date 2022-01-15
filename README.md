DotUtilities
=
A utilities plugin for developers. This plugin contains a chest menu API and a CommandHandler.



MenuAPI
-

First get the menu manager by using:
```java
DotUtilities.getMenuManager();
```

You can store the manager in a MenuManager object, or just use the `createMenu()` method and store the resulting menu. It is recommended to just create a menu, as that's the only method that's exposed.

### MenuManager
1. `createMenu() : Menu`
   - Create a new menu.

#### createMenu
1. `Player player`
   - The player to create the menu for.
2. `int size`
   - The number of slots in the menu (Must be a multiple of 9).
3. `String title`
   - The title shown at the top of the menu.

- `Menu`
  - The menu created from the parameters.

Creates a new Menu object based on the parameters provided. The title will be shown above the menu. The size must be a multiple of 9. If it is not it will be corrected to the closest multiple. Note that this will likely change to a rows parameter later.



### Menu
1. `setButton() : MenuButton`
   - Create a button on the menu.
2. `showMenu() : void`
   - Show the menu to the player.
3. `getPlayer() : Player`
   - Get the player this menu is for.
4. `markDestruction() : void`
   - Remove the menu from the manager so that the GC can collect it.
5. `getExists() : boolean`
   - Check if this menu has had markDestruction() called.
6. `compareInventory() : boolean`
   - Check if an inventory is the inventory for this menu.


#### setButton
1. `ItemStack item`
   - The item to display for the button.
2. `int x`
   - The x position of the button (0 based).
3. `int y`
   - The y position of the button (0 based).
4. `ButtonListener listener`
   - The ButtonListener object that should be called when the button is clicked in the menu.

- `MenuButton`
  - The button created from the parameters.

Creates a new button using the parameters and adds it to the menu in the position specified. Returns a MenuButton object with the new button.

#### showMenu

Shows the current menu to the player. Note if you modify the item stack of a button while it is being shown to the player you must call this again to update the displayed items, modifications to the button listener will be updated instantly).

#### getPlayer
- `Player`
  - The player that this menu was created for.

Returns the holder of this menu that was specified during it's creation.

#### markDestruction

Removes this menu from the menu tracker so that it can be allowed to go out of scope for the GC to collect. Note that trying to show a menu after calling this will throw an InteralMenuException.

#### getExists

Returns a boolean that is true if this menu is still active and false if it has been marked for destruction. Note that is a player quits the server a menu will automatically be marked for destruction.

#### compareInventory
1. `Inventory inventory`
   - The inventory to compare to.

- `boolean`
  - Whether or not the inventory is for this menu.

Returns a boolean that is true if the inventory provided is the inventory generated for the player by this menu.



### MenuButton
1. `setListener() : void`
   - Set the button listener for this MenuButton.
2. `setItem() : void`
   - Set the ItemStack object displayed for this button.
3. `getParent() : Menu`
   - Get the menu this button is for.


#### setListener
1. `ButtonListener listener`
   - The ButtonListener to call when the player clicks this button.

The buttonClicked method in the listener is called whenever the player clicks the button.

#### setItem
1. `ItemStack item`
   - The item to display for this button

Sets the item this button will display when the menu is shown to the player.

#### getParent
- `Menu`
  - The menu that this button belongs to.

Returns the menu object that owns this button.



### ButtonListener : Interface
1. `buttonClicked() : boolean`
   - The method to call when a button is clicked.


#### buttonClicked
- `boolean`
  - Whether or not to close the menu.

This method should return a boolean that is true if the menu should be closed after being clicked.




CommandHelper
-

TODO
