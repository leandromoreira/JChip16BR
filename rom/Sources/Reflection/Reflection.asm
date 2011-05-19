;Reflection for Chip16
;Shendo, May 2011
;
;r0 - Selector X coordinate
;r1 - Selector Y coordinate
;r2 - Unused
;r3 - Unused
;r4 - Current level
;r5 - Unused
;r6 - Unused
;r7 - Unused
;r8 - Pressed pad flag (0 - free, 1 - pressed)
;r9 - Stage over flag(0 - game going, 1 - stage beaten)
;ra - Scratchpad
;rb - Scratchpad
;rc - Scratchpad
;rd - Scratchpad
;re - Scratchpad
;rf - Scratchpad
;
;ASCII Index (0xFF is the string terminator):
;00 01 02 03 04 05 06 07 08 09 		HEX
;00 01 02 03 04 05 06 07 08 09		DEC
; 0  1  2  3  4  5  6  7  8  9		SPR
;
;0A 0B 0C 0D 0E 0F 10 11 12 13 14 15 16 17 18 19 1A 1B 1C 1D 1E 1F 20 21 22 23		HEX
;10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35		DEC
; A  B  C  D  E  F  G  H  I  J  K  L  M  N  O  P  Q  R  S  T  U  V  W  X  Y  Z		SPR
;
;24 25 26 27 28 29 2A 2B 2C 2D 2E 2F 30 31 32 33 34 35 36 37 38 39 3A 3B 3C 3D		HEX
;36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60 61		DEC
; a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z		SPR
;
;3E 3F 40 41 42 43 44 45 46 47		HEX
;62 63 64 65 66 67 68 69 70 71		DEC
;    !  -  /  ,  .  :  (  )  &		SPR
;
;Board coding:
;
;00 - No object
;01 - Start position
;02 - End position
;03 - Crate
;04 - Mirror 0 (Right)
;05 - Mirror 1 (Left)
;
;Light movement:
;
;00 - Up
;01 - Down
;02 - Left
;03 - Right
;

;GRAPHIC DATA

importbin logo.bin 0 2190 logo			;Graphic data for the "REFLECTION" logo
importbin divider.bin 0 32 stage_divider	;Graphic data for the stage divider "+" sprite
importbin selector.bin 0 722 selector		;Graphic data for the selector
importbin bulb.bin 0 128 bulb			;Graphic data for the light bulb
importbin crystal.bin 0 128 crystal		;Graphic data for the crystal
importbin crate.bin 0 128 crate			;Graphic data for the crate
importbin mirror_0.bin 0 128 mirror_0		;Graphic data for the right mirror
importbin mirror_1.bin 0 128 mirror_1		;Graphic data for the left mirror
importbin light.bin 0 72 light_beam		;Graphic data for light "beam"
importbin numbers_font.bin 0 320 numbers_font	;Font data (numbers)
importbin capitals_font.bin 0 832 capitals_font	;Font data (capital letters)
importbin lowcase_font.bin 0 832 lowcase_font	;Font data (lowcase letters)
importbin special_font.bin 0 320 special_font	;Font data (special characters)

;GAME DATA

importbin stage1.bin 0 112 stage_1		;Data for the stage 1
importbin stage2.bin 0 112 stage_2		;Data for the stage 2
importbin stage3.bin 0 112 stage_3		;Data for the stage 3
importbin stage4.bin 0 112 stage_4		;Data for the stage 4
importbin stage5.bin 0 112 stage_5		;Data for the stage 5
importbin stage6.bin 0 112 stage_6		;Data for the stage 6
importbin stage7.bin 0 112 stage_7		;Data for the stage 7
importbin stage8.bin 0 112 stage_8		;Data for the stage 8
importbin stage9.bin 0 112 stage_9		;Data for the stage 9
importbin stage10.bin 0 112 stage_10		;Data for the stage 10
importbin current_board.bin 0 112 current_board	;Data for the currently loaded board

:start						;Start of the program
	cls					;Clear screen for fresh start

:menu						;Game menu scene
	vblnk					;Wait for Vblank
	cls					;Clear screen
	
	spr #1E49				;Set 146x30 px sprites
	ldi ra, 87				;Load sprite X coordinate
	ldi rb, 50				;Load sprite Y coordinate
	drw ra, rb, logo			;Draw sprite on screen

	ldi re, 80				;Load string X coordinate
	ldi rf, 160				;Load string Y coordinate
	ldi rd, ascii_start			;Load string memory location
	call draw_string			;Draw string on screen

	ldi re, 30				;Load string X coordinate
	ldi rf, 225				;Load string Y coordinate
	ldi rd, ascii_credits			;Load string memory location
	call draw_string			;Draw string on screen

:menu_start_loop				;Pause untill START button has been pressed
	vblnk					;Wait for VBlank
	ldm ra, #FFF0				;Load PAD1 status to scratchpad
	andi ra, 32				;Isolate START button
	jmz menu_start_loop			;Draw another frame if start hasn't been pressed

:game_pre_init
	ldi r4, 0				;Load default level

:game_init					;Game init sequence
	vblnk					;Wait for VBlank
	cls					;Clear the screen

	call copy_board_to_current		;Set up board in memory

	ldi r0, 0				;Load default selector X coordinate
	ldi r1, 0				;Load default selector Y coordinate
	ldi r9, 0				;Stage is running

	mov ra, r4				;Copy current level to scratchpad
	addi ra, 1				;Convert to "normal folks readable"
	ldi rd, ascii_level_bcd			;Load current level BCD string address	
	call bcd_to_mem				;Store current level BCD data to memory

	ldi re, 118				;Load X coordinate
	ldi rf, 110				;Load Y coordinate
	ldi rd, ascii_level			;Load string memory location
	call draw_string			;Draw string on screen

	ldi ra, 120				;Load 120 frames pause
	call wait_frames			;Call wait subroutine

:loop						;The game loop
	vblnk					;Wait for VBlank
	cls					;Clear the screen
	call draw_playfield			;Draw game playfield
	call draw_light				;Draw path of light on screen
	call draw_objects			;Draw objects on screen
	call draw_selector			;Draw selector on screen
	call process_input			;React to PAD1 keys

	addi r9, 0				;Check if stage has been beaten
	jmz loop				;Draw new frame

	snd2 250				;Play 1000 Hz tone for 250 ms
	ldi ra, 60				;Set 60 frame pause
	call wait_frames			;Call wait frames subroutine
	addi r4, 1				;Increase level number

	mov ra, r4				;Copy current stage
	subi ra, 10				;Check if max stage has been reached
	jmz start				;Restart the game

	jmp game_init				;Stage beaten, start new match

;STRING DATA

:ascii_author					;Author's name in standard ASCII
	db #53			;S
	db #68			;h
	db #65			;e
	db #6E			;n
	db #64			;d
	db #6F			;o
	db #00			;/0

:ascii_start					;Message which instruct player to press start button
	db 25			;P
	db 53			;r
	db 40			;e
	db 54			;s
	db 54			;s
	db 62			; 
	db 28			;S
	db 29			;T
	db 10			;A
	db 27			;R
	db 29			;T
	db 62			; 
	db 37			;b
	db 56			;u
	db 55			;t
	db 55			;t
	db 50			;o
	db 49			;n
	db 255			;/0


:ascii_credits					;Credits
	db 12			;C
	db 50			;o
	db 39			;d
	db 40			;e
	db 68			;:
	db 28			;S
	db 43			;h
	db 40			;e
	db 49			;n
	db 39			;d
	db 50			;o
	db 66			;,
	db 62			; 
	db 21			;L
	db 40			;e
	db 57			;v
	db 40			;e
	db 47			;l
	db 54			;s
	db 68			;:
	db 10			;A
	db 59			;x
	db 44			;i
	db 54			;s
	db 35			;Z
	db 08			;8
	db 00			;0
	db 00			;0
	db 08			;8
	db 255			;/0

:ascii_level					;Current level string
	db 21			;L
	db 40			;e
	db 57			;v
	db 40			;e
	db 47			;l
	db 68			;:

:ascii_level_bcd				;Current level BCDs
	db 00			;0
	db 00			;0
	db 255			;/0


;SUBROUTINES

:draw_string					;Draw string on screen
	spr #0804				;Set 8x8 pixel sprites
	ldm ra, rd				;Load characted from memory
	andi ra, #FF				;Only the lo byte is needed

	mov rb, ra				;Copy data to scratchpad
	subi rb, 255				;Remove terminator
	jmz fret				;Terminator reached, break subroutine

	mov rb, ra				;Copy data to scratchpad
	muli rb, 32				;Each character is 32 bytes long
	addi rb, numbers_font			;Apply offset to font address

	drw re, rf, rb				;Draw 8x8 character on the set coordinates

	addi rd, 1				;Increase memory offset
	addi re, 9				;Increase X coordinate

	jmp draw_string

:copy_board_to_current				;Copy active board to current stage
	ldi ra, stage_1				;Get board starting address
	ldi rb, current_board			;Get current board starting address
	addi ra, 112				;Move pointer to end of data
	addi rb, 112				;Move pointer to end of data

	mov rc, r4				;Copy current stage
	muli rc, 112				;Convert to byte offset

	add ra, rc				;Apply current stage offset
	
	ldi rd, 112				;Byte counter
:copy_board_loop
	ldm rf, ra				;Load stage to buffer
	stm rf, rb				;Place stage from buffer

	subi ra, 2				;Decrease offset
	subi rb, 2				;Decrease offset
	subi rd, 2				;Decrease counter
	
	jmc fret				;Data is copied, break subroutine
	jmp copy_board_loop			;Copy next word

:wait_frames					;Wait for required number of frames
	vblnk					;Wait for VBlank
	subi ra, 1				;Decrement frame counter
	jmz fret				;Break the loop
	jmp wait_frames				;Wait for another frame

:bcd_to_mem					;Convert number to BCD and store it in memory
	mov rb, ra				;Copy numeric value
	divi rb, 10				;Isolate tens

	mov rc, rb				;Copy tens to scratchpad
	muli rc, 10				;Convert to tens
	sub ra, rc				;Get ones
	
	shl ra, 8				;Move ones to hi byte
	or rb, ra				;Add tens to value

	stm rb, rd				;Store string data to memory

	ret					;Return from subroutine

:process_input					;React to gamepad commands
	ldm ra, #FFF0				;Load Pad 1 status

	addi ra, 0				;Update flags
	jmz input_clr_flg			;Clear pad flag if no buttons are pressed
	jmp input_flg_check			;Flag shouldn't be cleared

:input_clr_flg					;Clear pad flag
	ldi r8, 0				;Update cleared status

:input_flg_check				;Check the status of pad flag
	addi r8, 0				;Update flags
	jmz input_action			;Proceed with key decoding
	ret					;Pad flag is up, break subroutine

:input_action					;Decide action on pressed key
	ldm ra, #FFF0				;Load Pad 1 status
	andi ra, 1				;Isolate up key
	subi ra, 1				;Remove offset
	jmz input_move_up			;Move selector up

	ldm ra, #FFF0				;Load Pad 1 status
	andi ra, 2				;Isolate down key
	subi ra, 2				;Remove offset
	jmz input_move_down			;Move selector down

	ldm ra, #FFF0				;Load Pad 1 status
	andi ra, 4				;Isolate left key
	subi ra, 4				;Remove offset
	jmz input_move_left			;Move selector left

	ldm ra, #FFF0				;Load Pad 1 status
	andi ra, 8				;Isolate right key
	subi ra, 8				;Remove offset
	jmz input_move_right			;Move selector right

	ldm ra, #FFF0				;Load Pad 1 status
	andi ra, 64				;Isolate A key
	subi ra, 64				;Remove offset
	jmz input_apply_action			;Apply action on selected object

	ret					;No relevant keys are pressed, break subroutine

:input_move_up					;Move selector up
	ldi r8, 1				;Raise Pad flag

	mov rb, r1				;Copy Y cordinate
	addi rb, 0				;Update flags
	jmz fret				;Min offset reached, break subroutine

	subi r1, 1				;Decrement Y coordinate

	snd1 50					;Play 500 Hz tone for 50 ms

	ret					;Return from subroutine

:input_move_down				;Move selector down
	ldi r8, 1				;Raise Pad flag

	mov rb, r1				;Copy Y cordinate
	subi rb, 5				;Check if max offset has been reached
	jmz fret				;Max offset reached, break subroutine

	addi r1, 1				;Increment Y coordinate

	snd1 50					;Play 500 Hz tone for 50 ms

	ret					;Return from subroutine

:input_move_left				;Move selector left
	ldi r8, 1				;Raise Pad flag

	mov rb, r0				;Copy X cordinate
	addi rb, 0				;Update flags
	jmz fret				;Min offset reached, break subroutine

	subi r0, 1				;Decrement X coordinate

	snd1 50					;Play 500 Hz tone for 50 ms

	ret					;Return from subroutine

:input_move_right				;Move selector right
	ldi r8, 1				;Raise Pad flag

	mov rb, r0				;Copy X cordinate
	subi rb, 7				;Check if max offset has been reached
	jmz fret				;Max offset reached, break subroutine

	addi r0, 1				;Increment X coordinate

	snd1 50					;Play 500 Hz tone for 50 ms

	ret					;Return from subroutine

:input_apply_action				;Apply acttion on selected object
	ldi r8, 1				;Raise Pad flag

	ldi rc, current_board			;Current board pointer
	addi rc, 16				;Skip header
	mov re, r1				;Copy Y coordinate
	muli re, 8				;Each row has 8 fields
	add re, r0				;Add X coordinate
	muli re, 2				;Word are 2 bytes long
	add rc, re				;Get the final data pointer

	ldm ra, rc				;Load data from memory
	subi ra, 4				;Check for mirror_0
	jmz input_mirror_0			;Flip mirror_0
	
	ldm ra, rc				;Load data from memory
	subi ra, 5				;Check for mirror_1
	jmz input_mirror_1			;Flip mirror_1

	ret					;No relevant data found, skip subroutine

:input_mirror_0					;Mirror_0 was encountered
	snd2 50					;Play 1000 Hz tone for 50 ms

	ldi ra, 5				;Load mirror_1 value
	stm ra, rc				;Store value to memory

	ret					;Return from subroutine

:input_mirror_1					;Mirror_1 was encountered
	snd2 50					;Play 1000 Hz tone for 50 ms

	ldi ra, 4				;Load mirror_0 value
	stm ra, rc				;Store value to memory

	ret					;Return from subroutine

:draw_light					;Draw path of light
	spr #0A05				;Set 10x10 px sprites
	
	ldi rc, current_board			;Current board pointer
	ldm ra, rc				;Load X coordinate

	addi rc, 2				;Point to next word in memory
	ldm rb, rc				;Load Y coordinate

	addi rc, 2				;Point to next word in memory
	ldm rf, rc				;Load light direction

:light_loop
	mov rd, ra				;Copy X coordinate
	mov re, rb				;Copy Y coordinate

	muli rd, 41				;Convert to screen offset
	muli re, 41				;Convert to screen offset


	addi rd, 14				;Apply offset
	addi re, 15				;Apply offset

	drw rd, re, light_beam			;Draw sprite on screen

:light_coll_check				;Check for collisions
	ldi rc, current_board			;Current board pointer
	addi rc, 16				;Skip header
	mov re, rb				;Copy Y coordinate
	muli re, 8				;Each row has 8 fields
	add re, ra				;Add current field
	muli re, 2				;Word are 2 bytes long
	add rc, re				;Get the final data pointer

	ldm rd, rc				;Load value from memory
	subi rd, 2				;Isolate end object
	jmz light_end_reached			;Light beam has reached a goal

	ldm rd, rc				;Load value from memory
	subi rd, 3				;Isolate crate object
	jmz fret				;Break subroutine, light beam ended

	ldm rd, rc				;Load value from memory
	subi rd, 4				;Isolate mirror_0 object
	jmz light_mirror_0			;Go to mirror_0 handle

	ldm rd, rc				;Load value from memory
	subi rd, 5				;Isolate mirror_1 object
	jmz light_mirror_1			;Go to mirror_1 handle
	

:light_dir_check				;Check direction variable
	mov rd, rf				;Copy direction var
	subi rd, 0				;Get up offset
	jmz light_move_up			;Jump to up routine

	mov rd, rf				;Copy direction var
	subi rd, 1				;Get down offset
	jmz light_move_down			;Jump to up routine

	mov rd, rf				;Copy direction var
	subi rd, 2				;Get left offset
	jmz light_move_left			;Jump to up routine

	mov rd, rf				;Copy direction var
	subi rd, 3				;Get right offset
	jmz light_move_right			;Jump to up routine

	ret					;Shouldn't be reached but placed for safety

:light_move_up					;Move beam up
	addi rb, 0				;Update flags
	jmz fret				;Break from subroutine if min Y value has been reached

	subi rb, 1				;Decrease Y coordinate
	ldi rf, 0				;Set direction to up
	jmp light_loop				;Draw next portion

:light_move_down				;Move beam down
	mov rd, rb				;Copy Y coordinate
	subi rd, 5				;Check if max value has been reached
	jmz fret				;Break from subroutine

	addi rb, 1				;Increase Y coordinate
	ldi rf, 1				;Set direction to down
	jmp light_loop				;Draw next portion

:light_move_left				;Move beam left
	addi ra, 0				;Update flags
	jmz fret				;Break from subroutine if min X value has been reached

	subi ra, 1				;Decrease X coordinate
	ldi rf, 2				;Set direction to left
	jmp light_loop				;Draw next portion

:light_move_right				;Move beam right
	mov rd, ra				;Copy X coordinate
	subi rd, 7				;Check if max value has been reached
	jmz fret				;Break from subroutine

	addi ra, 1				;Increase X coordinate
	ldi rf, 3				;Set direction to right
	jmp light_loop				;Draw next portion

:light_mirror_0					;Mirror_0 was encountered
	ldi re, 0				;Check up direction
	jme rf, re, light_move_right		;Reflect beam right

	ldi re, 1				;Check down direction
	jme rf, re, light_move_left		;Reflect beam left

	ldi re, 2				;Check left direction
	jme rf, re, light_move_down		;Reflect beam down

	ldi re, 3				;Check right direction
	jme rf, re, light_move_up		;Reflect beam up

	ret					;Shouldn't be reached but placed for safety

:light_mirror_1					;Mirror_1 was encountered
	ldi re, 0				;Check up direction
	jme rf, re, light_move_left		;Reflect beam left

	ldi re, 1				;Check down direction
	jme rf, re, light_move_right		;Reflect beam right

	ldi re, 2				;Check left direction
	jme rf, re, light_move_up		;Reflect beam up

	ldi re, 3				;Check right direction
	jme rf, re, light_move_down		;Reflect beam down

	ret					;Shouldn't be reached but placed for safety

:light_end_reached				;Light reached the end crystal
	ldi r9, 1				;Set stage beaten flag
	ret

:draw_selector					;Draw selector on screen
	spr #2613				;Set 38x38 px sprites

	mov ra, r0				;Copy X coordinate
	mov rb, r1				;Copy Y coordinate

	muli ra, 41				;Convert to screen coordinates
	muli rb, 41				;Convert to screen coordinates

	drw ra, rb, selector			;Draw selector on screen
	ret					;Return form a subroutine

:draw_objects					;Draw objects on screen
	ldi ra, 7				;Board X coordinate
	ldi rb, 5				;Board Y coordinate
	ldi re, current_board			;Point scratchpad to playfield data
	addi re, 110				;Move pointer to end of the data
	spr #1008				;Set 16x16 px sprites
:objects_loop_x
	mov rc, ra				;Copy current X coordinate
	mov rd, rb				;Copy current Y coordinate
	muli rc, 41				;Convert X to screen coordinates
	muli rd, 41				;Convert Y to screen coordinates
	addi rc, 11				;Add playfield offset to X coordinate
	addi rd, 12				;Add playfield offset to Y coordinate

	ldm rf, re				;Load value from playfield
	addi rf, 0				;Update flags
	jmz objects_after_draw			;Skip drawing if there is no data

	ldm rf, re				;Load value from playfield
	subi rf, 1				;Remove object offset
	jmz objects_bulb			;Draw bulb on screen (start position)

	ldm rf, re				;Load value from playfield
	subi rf, 2				;Remove object offset
	jmz objects_crystal			;Draw crystal on screen (end position)

	ldm rf, re				;Load value from playfield
	subi rf, 3				;Remove object offset
	jmz objects_crate			;Draw crate on screen

	ldm rf, re				;Load value from playfield
	subi rf, 4				;Remove object offset
	jmz objects_mirror_0			;Draw right mirror

	ldm rf, re				;Load value from playfield
	subi rf, 5				;Remove object offset
	jmz objects_mirror_1			;Draw left mirror

:objects_bulb
	drw rc, rd, bulb			;Draw bulb on screen
	jmp objects_after_draw			;Proceed with the execution

:objects_crystal
	drw rc, rd, crystal			;Draw crystal on screen
	jmp objects_after_draw			;Proceed with the execution

:objects_crate
	drw rc, rd, crate			;Draw crate on screen
	jmp objects_after_draw			;Proceed with the execution

:objects_mirror_0
	drw rc, rd, mirror_0			;Draw right mirror on screen
	jmp objects_after_draw			;Proceed with the execution

:objects_mirror_1
	drw rc, rd, mirror_1			;Draw left mirror on screen
	jmp objects_after_draw			;Proceed with the execution

:objects_after_draw				;Instructions after drawing
	subi re, 2				;Decrease playfield pointer
	subi ra, 1				;Decrement X pointer
	jmc objects_loop_y			;Check if all has been drawn on screen
	jmp objects_loop_x			;Draw another object

:objects_loop_y
	ldi ra, 7				;Board X coordinate
	subi rb, 1				;Decrement Y pointer
	jmc fret				;Check if all has been drawn on screen
	jmp objects_loop_x			;Draw another object

:draw_playfield					;Draw tiled playfield
	ldi ra, 278				;Default tile X coordinate
	ldi rb, 198				;Default tile Y coordinate
	spr #0804				;Set 8x8 px sprites

:draw_loop_x					;Draw loop for the X cordinate
	drw ra, rb, stage_divider		;Draw 8x8 tile
	subi ra, 40				;Decrease X coordinate
	jmc draw_loop_y				;Check if minimal X coordinate has been reached
	jmp draw_loop_x				;Draw next tile

:draw_loop_y					;Draw loop for the Y coordinate
	ldi ra, 278				;Default tile X coordinate
	subi rb, 40				;Decrease Y coordinate
	jmc fret				;Return from subroutine if all tiles have been drawn
	jmp draw_loop_x				;Draw tiles for X coordinate

:fret						;Subroutine return function
	ret					;Return from subroutine