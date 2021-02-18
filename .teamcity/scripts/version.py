with open(".teamcity/version.txt") as f:
    file_content = f.read().rstrip("\n")
    lines = file_content.split("\n")
values = []
for val in lines:
    words = val.split("=")
    values.append(words[1])

version_build = values[0]
version_major = values[1]
version_minor = values[2]
version_patch = values[3]

print("version_patch= "+version_build)
print("version_major= "+version_major)
print("version_minor= "+version_minor)
print("version_patch= "+version_patch)

print('%build.counter%')
build_number = '##teamcity[buildNumber \'{}.{}.{}\']'.format(version_major,version_minor,version_patch)
print(build_number)