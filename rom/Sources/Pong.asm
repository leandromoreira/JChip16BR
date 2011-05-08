;Pong clone for the Chip 16
;Shendo 2011
;
;r0 - Player 1 X coordinate
;r1 - Player 1 Y coordinate
;r2 - Player 1 score
;r3 - Player 2 X coordinate
;r4 - Player 2 Y coordinate
;r5 - Player 2 score
;r6 - Ball X position
;r7 - Ball Y position
;r8 - Ball X direction (0 - left, 1 - right)
;r9 - Ball Y direction (0 - still, 1 - up, 2 - down)
;ra - Scratchpad
;rb - Scratchpad
;rc - Subroutine parameter 1 / Return value
;rd - Subroutine parameter 2
;re - Pad 1 data
;rf - Pad 2 data
;

:start					;Start of the program
	cls				;Clear screen for a fresh start
	ldi r2, 0			;Player 1 default score
	ldi r5, 0			;Player 2 default score

:game_init				;Initialization part of the game
	ldi r0, 20			;Default Player 1 X coordinate
	ldi r1, 100			;Default Player 1 Y coordinate
	ldi r3, 296			;Default Player 2 X coordinate
	ldi r4, 100			;Default Player 2 Y coordinate
	ldi r6, 24			;Default ball X position
	ldi r7, 118			;Default ball Y position
	ldi r8, 1			;Default ball X direction (right)
	rnd r9, 1			;Randomize ball Y direction
	addi r9, 1			;Remove the still ball option

:game_loop				;Main game loop
	vblnk				;Wait for VBlank
	vblnk				;Wait for another VBlank (brings game to 30 FPS)
	cls				;Clear screen
	ldm re, #FFF0			;Read Pad 1 state
	ldm rf, #FFF2			;Read Pad 2 state

:draw_scores				;Draw player scores on screen
	spr #0F06			;12x15 px sprite
	ldi ra, 100			;Player 1 score X coordinate
	ldi rb, 20			;Player 1 score Y coordinate
	ldi rc, numbers			;Load default font address
	ldi rd, 90			;Load sprite byte size
	mul rd, r2			;Convert to 'char'
	add rc, rd			;Get the sprite addres in memory
	drw ra, rb, rc			;Draw sprite

	ldi ra, 206			;Player 2 score X coordinate
	ldi rc, numbers			;Load default font address
	ldi rd, 90			;Load sprite byte size
	mul rd, r5			;Convert to 'char'
	add rc, rd			;Get the sprite addres in memory
	drw ra, rb, rc			;Draw sprite

:draw_paddles
	spr #2802			;4x40 px sprite
	drw r0, r1, paddle		;Draw Player 1 paddle
	drw r3, r4, paddle		;Draw Player 2 paddle

:move_p1_up
	addi r1, 0			;Update flags
	jmz move_p1_down		;Check if Player 1 paddle is in the minimal allowed position
	mov rb, re			;Copy Pad 1 state to scratchpad
	andi rb, 1			;Check if UP is pressed on Pad 1
	jmz move_p1_down		;UP is not pressed
	subi r1, 4			;Decrease Y coordinate

:move_p1_down
	ldi rb, 200			;Load maximum allowed position
	jme r1, rb, move_p2_up		;Check if Player 1 paddle is in the maximum allowed position
	mov rb, re			;Copy Pad 1 state to scratchpad
	andi rb, 2			;Check if DOWN is pressed on Pad 1
	jmz move_p2_up			;DOWN is not pressed
	addi r1, 4			;Increase Y coordinate

:move_p2_up
	addi r4, 0			;Update flags
	jmz move_p2_down		;Check if Player 2 paddle is in the minimal allowed position
	mov rb, rf			;Copy Pad 2 state to scratchpad
	andi rb, 1			;Check if UP is pressed on Pad 2
	jmz move_p2_down		;UP is not pressed
	subi r4, 4			;Decrease Y coordinate

:move_p2_down
	ldi rb, 200			;Load maximum allowed position
	jme r4, rb, draw_ball		;Check if Player 2 paddle is in the maximum allowed position
	mov rb, rf			;Copy Pad 2 state to scratchpad
	andi rb, 2			;Check if DOWN is pressed on Pad 2
	jmz draw_ball			;DOWN is not pressed
	addi r4, 4			;Increase Y coordinate

:draw_ball
	spr #0402			;4x4 px sprite
	drw r6, r7, ball		;Draw ball
	jmc change_direction		;Collision happened, change ball direction
	jmp move_ball_x			;Move ball accross X axis

:change_direction
	mov ra, r6			;Copy ball X position to scratchpad
	subi ra, 50			;Check if ball X is lower then 50 px
	jmc xor_direction		;Direction should be changed

	mov ra, r6			;Copy ball X position to scratchpad
	subi ra, 250			;Check if ball X is higher then 50 px
	jmc move_ball_x			;Skip direction change

:xor_direction
	xori r8, 1			;Change ball X direction
	snd2 45				;Play 1000 Hz tone for 45 ms

:move_ball_x
	addi r8, 0			;Update flags
	jmz move_ball_left		;Movement is set to left

:move_ball_right
	addi r6, 4			;Increase X coordinate
	jmp move_ball_y			;Move ball accross Y axis

:move_ball_left
	subi r6, 4			;Decrease X coordinate

:move_ball_y
	addi r9, 0			;Update flags
	jmz check_bounds		;There is no ball Y movement

:move_ball_up
	mov ra, r9			;Copy ball Y direction to scratchpad
	andi ra, 1			;Isolate up movement
	jmz move_ball_down		;Ball should not move up
	subi r7, 4			;Decrease ball Y position

:move_ball_down
	mov ra, r9			;Copy ball Y direction to scratchpad
	andi ra, 2			;Isolate down movement
	jmz check_bounds		;Ball should not move down
	addi r7, 4			;Increase ball Y position

:check_bounds
	mov ra, r7			;Copy ball Y coordinate to scratchpad
	subi ra, 4			;Decrease ball Y offset
	jmc set_ball_down		;Check if ball is in the minimal position
	mov ra, r7			;Copy ball Y coordinate to scratchpad
	subi ra, 236			;Decrease ball Y offset
	jmc check_goal			;Ball is within bounds

:set_ball_up
	ldi r9, 1			;Set ball direction to up
	snd2 45				;Play 1000 Hz tone for 45 ms
	jmp check_goal			;Continue with regular execution

:set_ball_down
	ldi r9, 2			;Set ball direction to down
	snd2 45				;Play 1000 Hz tone for 45 ms
	jmp check_goal			;Continue with regular execution

:check_goal
	mov rb, r6			;Copy ball X position
	subi rb, 8			;Remove playfield offset
	jmc p2_scored			;Player 2 scored
	mov rb, r6			;Copy ball X position
	subi rb, 312			;Remove playfield offset
	jmc game_loop			;Draw new frame, otherwise Player 1 scored

:p1_scored				;Player 1 scored a goal
	addi r2, 1			;Add point to player 1
	jmp goal_aftermath		;Do a cleanup after goal

:p2_scored				;Player 2 scored a goal
	addi r5, 1			;Add point to player 2
	jmp goal_aftermath		;Do a cleanup after goal

:goal_aftermath
	snd1 500			;Play 500 Hz tone for 500 ms
	ldi rc, 30			;Load number of frames to wait
	call wait_frames		;Wait for required number of frames
	ldi ra, 10			;Load max score to scratchpad
	ldi rc, numbers			;Load font address
	addi rc, 90			;Increase offset for '1' character
	jme r2, ra, game_over		;Check if Player 1 has max score
	addi rc, 90			;Increase offset for '2' character
	jme r5, ra, game_over		;Check if Player 2 has max score
	jmp game_init			;Start new round

:game_over				;Match is over
	cls				;Clear the screen
	spr #0F2E			;92x15 px sprite
	ldi ra, 114			;Load sprite X coordinate
	ldi rb, 112			;Load sprite Y coordinate
	drw ra, rb, won_sprite		;Draw sprite on screen

	spr #0F06			;12x15 px sprite
	addi ra, 16			;Increase X coordinate
	drw ra, rb, rc			;Draw sprite on screen
	ldi rc, 240			;Wait 240 frames (4 seconds)
	call wait_frames		;Call wait frames subroutine
	jmp start			;Start a new match


;SUBROUTINES

:wait_frames				;Wait for required number of frames // void WaitFrames(ushort FramesToWait)
	vblnk				;Wait for VBlank
	subi rc, 1			;Decrement frame counter
	jmz fret			;Counter is zero, jump to return function
	jmp wait_frames			;Do another loop

:fret					;Subroutine return function
	ret				;Return from a subroutine

;GRAPHIC DATA

:ball					;Ball sprite
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
:paddle					;Paddle sprite
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
:won_sprite				;"P WON"
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #FF
db #F0
db #00
db #00
db #00
db #FF
db #F0
db #00
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #FF
db #F0
db #00
db #00
db #00
db #FF
db #F0
db #00
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #FF
db #F0
db #00
db #00
db #00
db #FF
db #F0
db #00
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #FF
db #F0
db #00
db #00
db #00
db #FF
db #F0
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #00
db #00
db #FF
db #FF
db #FF
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #FF
db #F0
db #00
db #00
db #00
db #FF
db #F0
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #00
db #00
db #FF
db #FF
db #FF
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #FF
db #F0
db #00
db #00
db #00
db #FF
db #F0
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #00
db #00
db #FF
db #FF
db #FF
db #00
db #0F
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #FF
db #F0
db #0F
db #FF
db #00
db #FF
db #F0
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #00
db #00
db #FF
db #F0
db #00
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #FF
db #F0
db #0F
db #FF
db #00
db #FF
db #F0
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #00
db #00
db #FF
db #F0
db #00
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #FF
db #F0
db #0F
db #FF
db #00
db #FF
db #F0
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #00
db #00
db #FF
db #F0
db #00
db #FF
db #FF
db #FF
db #FF
db #F0
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #FF
db #F0
db #0F
db #FF
db #00
db #FF
db #F0
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #00
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #FF
db #F0
db #0F
db #FF
db #00
db #FF
db #F0
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #00
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #FF
db #F0
db #0F
db #FF
db #00
db #FF
db #F0
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #00
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #0F
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #0F
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #00
db #0F
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
:numbers					;Graphic data for numbers
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #FF
db #FF
db #FF
db #FF
db #F0
db #00
db #FF
db #FF
db #FF
db #FF
db #F0
db #00
db #FF
db #FF
db #FF
db #FF
db #F0
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #F0
db #00
db #00
db #00
db #00
db #FF
db #F0
db #00
db #00
db #00
db #00
db #FF
db #F0
db #00
db #00
db #00
db #00
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #F0
db #00
db #00
db #00
db #00
db #FF
db #F0
db #00
db #00
db #00
db #00
db #FF
db #F0
db #00
db #00
db #00
db #00
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #F0
db #00
db #00
db #00
db #00
db #FF
db #F0
db #00
db #00
db #00
db #00
db #FF
db #F0
db #00
db #00
db #00
db #00
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #FF
db #F0
db #00
db #00
db #00
db #00
db #FF
db #F0
db #00
db #00
db #00
db #00
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #F0
db #00
db #00
db #0F
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #00
db #00
db #00
db #00
db #0F
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF
db #FF