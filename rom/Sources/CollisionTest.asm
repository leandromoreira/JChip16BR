;Collision test for Chip16
;Shendo 2011
;
;Changelog:
;	Ver 1.1  04/18/2011
;	Fixed a bug which allowed player to go offscreen.
;
;r0 - Player X position
;r1 - Player Y position
;r2 - Player max X position
;r3 - Player max Y position
;r4 - RND generated X1 position
;r5 - RND generated Y1 position
;r6 - RND generated X2 position
;r7 - RND generated Y2 position
;r8 - RND generated X3 position
;r9 - RND generated Y3 position
;r6 - Sprite counter
;ra - PAD1 state
;rb - PAD1 state for manipulation
;
	cls				;Start clean
	spr #0804			;8x8 px sprites
	ldi r0, 158			;Default player X position
	ldi r1, 118			;Default player Y position
	ldi r2, 312			;Maximum player X position
	ldi r3, 232			;Maximum player Y position
	rnd r4, 312			;Generate block X1 position
	rnd r5, 232			;Generate block Y1 position
	rnd r6, 312			;Generate block X2 position
	rnd r7, 232			;Generate block Y2 position
	rnd r8, 312			;Generate block X3 position
	rnd r9, 232			;Generate block Y3 position
:loop
	vblnk				;Wait for VBlank
	vblnk				;Wait for VBlank
	cls				;Clear screen
	bgc #B				;Dark blue background
	ldm ra,#FFF0			;Read PAD1 state
:check_up
	mov rb,ra			;Copy PAD1 state to rb
	andi rb,1			;Isolate UP key
	jmz check_down			;If the button is not pressed, skip moving up
	addi r1,0			;Update flags
	jmz check_down			;Skip moving if min Y value has been reached
	subi r1,2			;Move sprite up
:check_down
	mov rb,ra			;Copy PAD1 state to rb
	andi rb,2			;Isolate DOWN key
	jmz check_left			;If the button is not pressed, skip moving down
	jme r1,r3,check_left		;Check if max Y value has been reached
	addi r1,2			;Move sprite down
:check_left
	mov rb,ra			;Copy PAD1 state to rb
	andi rb,4			;Isolate LEFT key
	jmz check_right			;If the button is not pressed, skip moving left
	addi r0,0			;Update flags
	jmz check_right			;Skip moving if the min X value has been reached
	subi r0,2			;Move sprite left
:check_right
	mov rb,ra			;Copy PAD1 state to rb
	andi rb,8			;Isolate RIGHT key
	jmz draw_sprite			;If the button is not pressed, skip moving right
	jme r0,r2,draw_sprite		;Check if max X value has been reached
	addi r0,2			;Move sprite right
:draw_sprite
	drw r4,r5,player_gray		;Draw 1st generated block sprite
	drw r6,r7,player_gray		;Draw 2nd generated block sprite
	drw r8,r9,player_gray		;Draw 3rd generated block sprite
	drw r0,r1,player_green		;Draw green player sprite
	jmc draw_red_sprite		;Collision happened, draw red sprite
	jmp loop			;Draw new frame
:draw_red_sprite
	drw r0,r1,player_red		;Draw red player sprite
	jmp loop			;Draw new frame
:player_green
db #00
db #0A
db #A0
db #00
db #00
db #0A
db #A0
db #00
db #00
db #0A
db #A0
db #00
db #AA
db #AA
db #AA
db #AA
db #AA
db #AA
db #AA
db #AA
db #00
db #0A
db #A0
db #00
db #00
db #0A
db #A0
db #00
db #00
db #0A
db #A0
db #00
:player_red
db #00
db #03
db #30
db #00
db #00
db #03
db #30
db #00
db #00
db #03
db #30
db #00
db #33
db #33
db #33
db #33
db #33
db #33
db #33
db #33
db #00
db #03
db #30
db #00
db #00
db #03
db #30
db #00
db #00
db #03
db #30
db #00
:player_gray
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22
db #22