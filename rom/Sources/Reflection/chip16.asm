;Reflection for Chip16
;2011 Shendo
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

importbin divider.bin 0 32 stage_divider	;Graphic data for the stage divider "+" sprite
importbin selector.bin 0 722 selector		;Graphic data for the selector
importbin bulb.bin 0 128 bulb			;Graphic data for the light bulb
importbin crystal.bin 0 128 crystal		;Graphic data for the crystal
importbin crate.bin 0 128 crate			;Graphic data for the crate
importbin mirror_0.bin 0 128 mirror_0		;Graphic data for the right mirror
importbin mirror_1.bin 0 128 mirror_1		;Graphic data for the left mirror
importbin light.bin 0 72 light_beam		;Graphic data for light "beam"
importbin level.bin 0 290 level			;Graphic data for "LEVEL" string
importbin numbers.bin 0 590 numbers		;Graphic data for number sprites

;GAME DATA

importbin stage1.bin 0 112 stage_1		;Data for the stage 1
importbin stage2.bin 0 112 stage_2		;Data for the stage 2
importbin stage3.bin 0 112 stage_3		;Data for the stage 3
importbin stage4.bin 0 112 stage_4		;Data for the stage 4
importbin stage5.bin 0 112 stage_5		;Data for the stage 5
importbin current_board.bin 0 112 current_board	;Data for the currently loaded board

:start						;Start of the program
	cls					;Clear screen for fresh start

:intro						;Game intro scene
	nop					;Placeholder for now

:menu						;Game menu scene
	nop					;Placeholder for now

:game_pre_init
	ldi r4, 0				;Load default level

:game_init					;Game init sequence
	vblnk					;Wait for VBlank
	cls					;Clear the screen

	call copy_board_to_current		;Set up board in memory

	ldi r0, 0				;Load default selector X coordinate
	ldi r1, 0				;Load default selector Y coordinate
	ldi r9, 0				;Stage is running

	call draw_level_string			;Draw current level information

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
	subi ra, 5				;Check if max stage has been reached
	jmz game_pre_init			;Restart the game

	jmp game_init				;Stage beaten, start new match

;AUTHOR INFO

db #53						;"Shendo"
db #68
db #65
db #6E
db #64
db #6F
db #00

;SUBROUTINES

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

:draw_level_string				;Display current level on screen
	spr #0A1D				;Set 58x10 px sprites

	ldi ra, 110				;Load X coordinate
	ldi rb, 110				;Load Y coordinate

	drw ra, rb, level			;Draw "LEVEL" string on screen

:level_numbers					;Draw sprites
	spr #0A05				;Set 10x10 px sprites

	ldi ra, 180				;Load X coordinate
	ldi rb, 110				;Load Y coordinate

	mov rc, r4				;Copy current level
	mov re, r4				;Copy current level
	addi rc, 1				;Convert to regular numbers (normal folks readable :p)
	addi re, 1				;Convert to regular numbers

	divi rc, 10				;Get tens
	mov rf, rc				;Copy tens
	muli rf, 10				;Get tens without ones
	muli rc, 60				;Get the byte offset

	ldi rd, numbers				;Load number sprite location
	add rd, rc				;Get number offset

	drw ra, rb, rd				;Draw number on screen (tens)

	addi ra, 12				;Increase X coordinate

	sub re, rf				;Remove tens from result
	muli re, 60				;Get the byte offset

	ldi rd, numbers				;Load number sprite location
	add rd, re				;Get the byte offset

	drw ra, rb, rd				;Draw number on screen (ones)

	ret

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