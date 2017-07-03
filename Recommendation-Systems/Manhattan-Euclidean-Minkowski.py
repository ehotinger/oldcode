from math import sqrt

# If your data is dense (almost all attributes have non-zero values) and the magnitude of the attribute values is important,
# use distance measures like Euclidean & Manhattan.

users = {"Eric": {"Radioactive": 3.5, "On Top of the World": 2.0, "Hello World": 5.0},
		 "Tony": {"Radioactive": 2.0, "On Top of the World": 3.5, "Another": 4},
		 "Greg": {"None in Common": 5.0}}



# Computing Manhattan Distance.
# Advantages: Fast to compute and useful to answer the question "Who is similar to Person X?"
# Works best with no missing values; results may be skewed between users who have missing values.
# For example, consider Greg who has nothing in common with Eric and Tony - their distance is 0 which isn't quite accurate.
def manhattan(rating1, rating2):
	distance = 0
	for key in rating1:
		if key in rating2:
			distance += abs(rating1[key] - rating2[key])
	return distance


#print manhattan(users["Eric"], users["Tony"]) # 3.0
#print manhattan(users["Greg"], users["Eric"]) # 0

# Euclidean Distance
def euclidean(rating1, rating2):
	distance = 0
	for key in rating1:
		if key in rating2:
			distance += pow(rating1[key] - rating2[key], 2)
	return sqrt(distance)


# The greater the r, the more a large difference in one dimension
# will influence the total difference.
# In the event of no commonalities between two users,
# we'll return 0.
def minkowski(rating1, rating2, r):
	distance = 0
	hasCommonRating = False

	for key in rating1:
		if key in rating2:
			hasCommonRating = True
			distance += pow(abs(rating1[key] - rating2[key]), r)
	
	if hasCommonRating:
		return pow(distance, 1/r)

	else: 
		return 0

#print euclidean(users["Eric"], users["Tony"]) # 2.12132034356
#print euclidean(users["Greg"], users["Tony"]) # 0

def getNearestNeighborManhattan(username, users):
	distances = []
	for user in users:
		if user != username:
			distance = manhattan(users[user], users[username])
			distances.append((distance, user))
	distances.sort()
	return distances

def getNearestNeighborMinkowski(username, users, r):
	distances = []
	for user in users:
		if user != username:
			distance = minkowski(users[user], users[username], r)
			distances.append((distance, user))
	distances.sort()
	return distances

#print getNearestNeighborManhattan("Eric", users) # [(0, 'Greg'), (3.0, 'Tony')]
#print getNearestNeighborManhattan("Tony", users) # [(0, 'Greg'), (3.0, 'Eric')]
#print getNearestNeighborManhattan("Greg", users) # [(0, 'Eric'), (0, 'Tony')]
#print getNearestNeighborMinkowski("Eric", users, 1)
#print getNearestNeighborMinkowski("Eric", users, 2)
#print getNearestNeighborMinkowski("Tony", users, 1)