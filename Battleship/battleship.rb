def init
  @grid = Array.new(10) {Array.new(10) {false}}
  #patrol boat - 2 cells; assumed ships belong to one player for now
  @patrol = [[0,0], [0,1]]
  #submarine - 3 cells
  @submarine = [[1,0], [1,1], [1,2]]
  
end

def shoot(x, y)
  loc = Array.new
  loc << x << y
  ship = nil
  name = ""
  if @patrol.include?(loc) #if true, there is a hit on patrol boat
    ship = @patrol
    name = "partrol boat"
  end
    
  if (ship == nil and @submarine.include?(loc)) #if true, there is a hit on the submarine
    ship = @submarine
    name = "submarine"
  end
  
  if ship == nil 
    puts "missed!"
    return
  end 
  
  #there is a hit at this point, verify if the hit result in sinking the ship
  row = @grid[x][y] = true #a hit; update the cell with true status
  sunk = true
  ship.each do |box|
    # puts "#{box.to_s}: #{@grid[box[0]][box[1]]}"
    sunk &= @grid[box[0]][box[1]]
  end
  if sunk
    puts "#{name} sunk!"
  else
    puts "hit!"
  end
end
