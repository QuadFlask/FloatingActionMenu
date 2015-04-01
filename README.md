FloatingActionMenu
--------

FloatingActionButton with menu like Google's Inbox app style.


## Demo video

[Youtube](https://youtu.be/zjW2SDJOrOk)


## Screenshot

![screenshot1](https://github.com/QuadFlask/FloatingActionMenu/blob/master/captures/device-2015-04-01-163215.png)

![screenshot2](https://github.com/QuadFlask/FloatingActionMenu/blob/master/captures/device-2015-04-01-163240.png)


## Usage

Create from code is not tested, so you should use xml. 

```xml
<com.flask.floatingactionmenu.FloatingActionButton
		android:id="@+id/fab1"
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		android:layout_alignParentBottom="true"
		android:layout_alignParentLeft="true"
		android:layout_margin="16dp"
		fab:fab_normal_icon="@drawable/ic_add_white_24dp"
		/>

	<com.flask.floatingactionmenu.FloatingActionButton
		android:id="@+id/fab2"
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		android:layout_alignParentTop="true"
		android:layout_alignParentRight="true"
		android:layout_margin="16dp"
		fab:fab_normal_icon="@drawable/ic_add_white_18dp"
		fab:fab_type="mini"
		/>

	<com.flask.floatingactionmenu.FloatingActionToggleButton
		android:id="@+id/fab3"
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		android:layout_alignParentBottom="true"
		android:layout_centerHorizontal="true"
		android:layout_margin="16dp"
		fab:fab_normal_icon="@drawable/ic_add_white_24dp"
		fab:fab_toggle_icon="@drawable/ic_mode_edit_white_24dp"
		/>

	<com.flask.floatingactionmenu.FadingBackgroundView
		android:id="@+id/fading"
		android:layout_width="match_parent"
		android:layout_height="match_parent"/>

	<com.flask.floatingactionmenu.FloatingActionMenu
		android:id="@+id/fam"
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		android:layout_alignParentBottom="true"
		android:layout_alignParentRight="true"
		android:layout_margin="4dp"
		fab:fab_labelStyle="@style/fab_labels_style"
		>

		<com.flask.floatingactionmenu.FloatingActionButton
			android:id="@+id/faba"
			android:layout_width="wrap_content"
			android:layout_height="wrap_content"
			android:layout_margin="8dp"
			fab:fab_normal_icon="@drawable/ic_add_white_18dp"
			fab:fab_type="mini"
			fab:fab_labelText="faba"
			/>

		<com.flask.floatingactionmenu.FloatingActionButton
			android:id="@+id/fabb"
			android:layout_width="wrap_content"
			android:layout_height="wrap_content"
			android:layout_margin="8dp"
			fab:fab_normal_icon="@drawable/ic_mode_edit_white_18dp"
			fab:fab_type="mini"
			fab:fab_labelText="faba"
			/>

		<com.flask.floatingactionmenu.FloatingActionButton
			android:id="@+id/fabc"
			android:layout_width="wrap_content"
			android:layout_height="wrap_content"
			android:layout_margin="8dp"
			fab:fab_normal_icon="@drawable/ic_add_white_18dp"
			fab:fab_type="mini"
			fab:fab_labelText="fabc"
			/>

		<com.flask.floatingactionmenu.FloatingActionToggleButton
			android:id="@+id/fab_toggle"
			android:layout_width="wrap_content"
			android:layout_height="wrap_content"
			android:layout_marginTop="8dp"
			fab:fab_normal_icon="@drawable/ic_add_white_24dp"
			fab:fab_toggle_icon="@drawable/ic_mode_edit_white_24dp"
			fab:fab_labelText="toggle button"
			/>
	</com.flask.floatingactionmenu.FloatingActionMenu>
```


## To do

* gradle support
* performance improvement
* refactoring
* other label style


## Credits

I used [android-floating-action-button](https://github.com/futuresimple/android-floating-action-button) library by [futuresimple](https://github.com/futuresimple) and [FloatingActionButton](https://github.com/makovkastar/FloatingActionButton) library by [Oleksandr Melnykov](https://github.com/makovkastar) as a base for development.


## License

```
Copyright 2015 QuadFlask

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
