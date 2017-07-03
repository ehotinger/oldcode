# If the data is sparse consider usign Cosine similarity.

from math import sqrt

# Popular in text mining and used in collaborative filtering
# Let's keep track of the number of plays for each user.
# Cosine similarity ignores 0-0 matches
users = {"Eric": {"Radioactive": 1, "On Top of the World": 1, "Hello World": 1},
		 "Michael": {"Radioactive": 10, "On Top of the World": 10, "Hello World": 10},
		 "Greg": {"Radioactive": 12, "On Top of the World": 6, "Hello World": 27}
		 }

def cosineSimilarity(rating1, rating2):
	vectorXLength = 0
	vectorYLength = 0
	dotProduct = 0
	for key in rating1:
		if key in rating2:
			vectorXLength += rating1[key]**2
			vectorYLength += rating2[key]**2
			dotProduct += rating1[key] * rating2[key]

	return dotProduct / (sqrt(vectorXLength) * sqrt(vectorYLength))


print cosineSimilarity(users["Eric"], users["Michael"])